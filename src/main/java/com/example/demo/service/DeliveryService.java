package com.example.demo.service;

import com.example.demo.dto.DeliveryDTO;
import com.example.demo.dto.ProductOrderDTO;
import com.example.demo.dto.VanDTO;
import com.example.demo.entity.Delivery;
import com.example.demo.entity.Van;
import com.example.demo.errorhandling.exception.NotFoundException;
import com.example.demo.errorhandling.exception.ValidationException;
import com.example.demo.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    DeliveryRepository deliveryRepository;
    ProductOrderService productOrderService;
    VanService vanService;

    public DeliveryService(DeliveryRepository deliveryRepository, ProductOrderService productOrderService, VanService vanService) {
        this.deliveryRepository = deliveryRepository;
        this.productOrderService = productOrderService;
        this.vanService = vanService;
    }

    public Delivery toEntity(DeliveryDTO deliveryDTO) {
        var productOrderDTOS = deliveryDTO
                .getProductOrders();
        var productOrders = productOrderDTOS
                .stream()
                .map(productOrderService::toEntity)
                .toList();
        Van van = null;
        if (deliveryDTO.getVanId() != null) {
            var vanDto = vanService
                    .getById(deliveryDTO.getVanId())
                    .orElseThrow(() -> new ValidationException("Van does not exist."));
            van = vanService.toEntity(vanDto);
        }

        Delivery delivery = new Delivery();
        delivery.setId(deliveryDTO.getId());
        delivery.setVan(van);
        delivery.setProductOrders(productOrders);
        delivery.setDestination(deliveryDTO.getDestination());
        delivery.setFromWarehouse(deliveryDTO.getFromWarehouse());
        delivery.setDeliveryDate(deliveryDTO.getDeliveryDate());
        return delivery;
    }

    public DeliveryDTO toDTO(Delivery delivery) {
        DeliveryDTO deliveryDTO = new DeliveryDTO();
        deliveryDTO.setId(delivery.getId());
        if ((delivery.getVan() != null)) {
            deliveryDTO.setVanId(delivery.getVan().getId());
        }
        deliveryDTO.setProductOrders(delivery.getProductOrders().stream().map(productOrderService::toDTO).toList());
        deliveryDTO.setDestination(delivery.getDestination());
        deliveryDTO.setFromWarehouse(delivery.getFromWarehouse());
        deliveryDTO.setDeliveryDate(delivery.getDeliveryDate());
        deliveryDTO.setTotalPrice(delivery.getTotalPrice());
        deliveryDTO.setTotalWeightInKg(delivery.getTotalWeightInKg());
        return deliveryDTO;
    }

    public List<DeliveryDTO> getAll(Boolean assignedToVan) {
        if (assignedToVan == null) {
            return getAll();
        }
        if (assignedToVan) {
            return deliveryRepository
                    .findAllByVanIsNotNull()
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }
        return deliveryRepository
                .findAllByVanIsNull()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<DeliveryDTO> getAll() {
        return deliveryRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<DeliveryDTO> getById(Long id) {
        var delivery = deliveryRepository.findById(id);
        return delivery.map(this::toDTO);
    }

    public DeliveryDTO save(DeliveryDTO deliveryDTO) {
        var delivery = toEntity(deliveryDTO);
        if(delivery.getVan() != null) {
            vanService.addDelivery(delivery.getVan().getId(), deliveryDTO);
        }
        delivery
                .setProductOrders(delivery
                        .getProductOrders()
                        .stream()
                        .map(productOrderService::toDTO)
                        .map(productOrderService::save)
                        .map(productOrderService::toEntity)
                        .toList());

        var newDelivery = deliveryRepository.save(delivery);
        return toDTO(newDelivery);
    }

    public DeliveryDTO assignVan(Long id, VanDTO van) {
        if (van == null) {
            throw new ValidationException("Van is required.");
        }
        if(van.getId() == null) {
            throw new ValidationException("Id of van is required.");
        }
        var deliveryToUpdate = deliveryRepository.findById(id).orElseThrow(NotFoundException::new);
        var vanDto = vanService
                .getById(van.getId())
                .orElseThrow(() -> new ValidationException("Van does not exist."));
        var vanToAssign = vanService.toEntity(vanDto);
        vanService.addDelivery(vanToAssign.getId(), toDTO(deliveryToUpdate));
        deliveryToUpdate.setVan(vanToAssign);
        var updatedDelivery = deliveryRepository.save(deliveryToUpdate);
        return toDTO(updatedDelivery);
    }

    public List<ProductOrderDTO> getProductOrders(Long id) {
        var delivery = deliveryRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
        var productOrders = delivery.getProductOrders();
        return productOrders
                .stream()
                .map(productOrderService::toDTO)
                .toList();
    }

    public DeliveryDTO addProductOrders(Long id, List<ProductOrderDTO> productOrderDTOs) {
        var deliveryToUpdate = deliveryRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
        var productOrders = productOrderDTOs.stream()
                .map(productOrderService::toEntity)
                .toList();
        if (!deliveryToUpdate.hasCapacityForProductOrders(productOrders)) {
            throw new ValidationException("Orders could not be added to delivery. Van assigned to delivery does not have enough capacity.");
        }
        productOrderDTOs.forEach(productOrderService::save);
        deliveryToUpdate.getProductOrders().addAll(productOrders);
        var updatedDelivery = deliveryRepository.save(deliveryToUpdate);
        return toDTO(updatedDelivery);
    }

    public DeliveryDTO removeProductOrder(Long id, Long productOrderId) {
        var deliveryToUpdate = deliveryRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
        productOrderService
                .getById(productOrderId)
                .orElseThrow(() -> new ValidationException("Order does not exist."));
        var productOrderToRemove = deliveryToUpdate
                .getProductOrders()
                .stream()
                .filter(o -> o.getId().equals(productOrderId))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Order does not exist in delivery."));
        deliveryToUpdate
                .getProductOrders()
                .remove(productOrderToRemove);
        productOrderService.delete(productOrderId);
        var updatedDelivery = deliveryRepository.save(deliveryToUpdate);
        return toDTO(updatedDelivery);
    }

    public void delete(Long id) {
        deliveryRepository.deleteById(id);
    }
}
