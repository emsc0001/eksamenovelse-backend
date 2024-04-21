package com.example.demo.service;

import com.example.demo.dto.DeliveryDTO;
import com.example.demo.dto.VanDTO;
import com.example.demo.entity.Van;
import com.example.demo.errorhandling.exception.NotFoundException;
import com.example.demo.errorhandling.exception.ValidationException;
import com.example.demo.repository.VanRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VanService {

    VanRepository vanRepository;
    DeliveryService deliveryService;

    public VanService(VanRepository vanRepository, @Lazy DeliveryService deliveryService) {
        this.vanRepository = vanRepository;
        this.deliveryService = deliveryService;
    }

    public Van toEntity(VanDTO vanDTO) {
        Van van = new Van();
        van.setId(vanDTO.getId());
        van.setBrand(vanDTO.getBrand());
        van.setCapacityInKilograms(vanDTO.getCapacityInKilograms());
        van.setModel(vanDTO.getModel());
        return van;
    }

    public VanDTO toDTO(Van van) {
        VanDTO vanDTO = new VanDTO();
        vanDTO.setId(van.getId());
        vanDTO.setBrand(van.getBrand());
        vanDTO.setCapacityInKilograms(van.getCapacityInKilograms());
        vanDTO.setModel(van.getModel());
        return vanDTO;
    }

    public List<VanDTO> getAll() {
        return vanRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<VanDTO> getById(Long id) {
        var van = vanRepository.findById(id);
        return van.map(this::toDTO);
    }

    public List<DeliveryDTO> getDeliveries(Long id) {
        var van = vanRepository.findById(id).orElseThrow(NotFoundException::new);
        return van.getDeliveries().stream().map(deliveryService::toDTO).toList();
    }

    public VanDTO save(VanDTO vanDTO) {
        var van = toEntity(vanDTO);
        van = vanRepository.save(van);
        return toDTO(van);
    }

    public VanDTO update(Long id, VanDTO vanDTO) {
        var vanToUpdate = vanRepository.findById(id).orElseThrow(NotFoundException::new);
        var newVan = toEntity(vanDTO);

        vanToUpdate.setBrand(newVan.getBrand());
        vanToUpdate.setCapacityInKilograms(newVan.getCapacityInKilograms());
        vanToUpdate.setModel(newVan.getModel());

        var updatedVan = vanRepository.save(vanToUpdate);
        return toDTO(updatedVan);
    }

    public VanDTO addDelivery(Long id, DeliveryDTO deliveryDTO) {
        if(deliveryDTO == null) {
            throw new ValidationException("Delivery is required.");
        }
        if(deliveryDTO.getId() == null) {
            throw new ValidationException("Id of delivery is required.");
        }
        var vanToUpdate = vanRepository.findById(id).orElseThrow(NotFoundException::new);
        var delivery = deliveryService
                .getById(deliveryDTO.getId())
                .map(deliveryService::toEntity)
                .orElseThrow(() -> new ValidationException("Delivery does not exist."));
        if(!vanToUpdate.hasCapacityForDelivery(delivery)) {
            throw new ValidationException("Van does not have enough capacity.");
        }
        vanToUpdate
                .getDeliveries()
                .add(delivery);
        var updatedVan = vanRepository.save(vanToUpdate);
        return toDTO(updatedVan);
    }

    public void delete(Long id) {
        vanRepository.deleteById(id);
    }
}
