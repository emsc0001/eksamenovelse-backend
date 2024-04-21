package com.example.demo.service;

import com.example.demo.dto.ProductOrderDTO;
import com.example.demo.entity.ProductOrder;
import com.example.demo.errorhandling.exception.ValidationException;
import com.example.demo.repository.ProductOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductOrderService {

    ProductOrderRepository productOrderRepository;
    ProductService productService;

    public ProductOrderService(ProductOrderRepository productOrderRepository, ProductService productService) {
        this.productOrderRepository = productOrderRepository;
        this.productService = productService;
    }

    public ProductOrder toEntity(ProductOrderDTO productOrderDTO) {
        var productDto = productService
                .getById(productOrderDTO.getProductId())
                .orElseThrow(() -> new ValidationException("Product does not exist"));
        var product = productService.toEntity(productDto);
        ProductOrder productOrder = new ProductOrder();
        if(productOrderDTO.getId() != null){
            productOrder.setId(productOrderDTO.getId());
        }
        productOrder.setProduct(product);
        productOrder.setQuantity(productOrderDTO.getQuantity());
        return productOrder;
    }

    public ProductOrderDTO toDTO(ProductOrder productOrder) {
        ProductOrderDTO productOrderDTO = new ProductOrderDTO();
        if(productOrder.getId() != null){
            productOrderDTO.setId(productOrder.getId());
        }
        productOrderDTO.setProductId(productOrder.getProduct().getId());
        productOrderDTO.setQuantity(productOrder.getQuantity());
        return productOrderDTO;
    }

    public List<ProductOrderDTO> getAll() {
        return productOrderRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<ProductOrderDTO> getById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        var productOrder = productOrderRepository.findById(id);
        return productOrder.map(this::toDTO);
    }

    public ProductOrderDTO save(ProductOrderDTO productOrderDTO) {
        var productOrder = toEntity(productOrderDTO);
        productOrder = productOrderRepository.save(productOrder);
        return toDTO(productOrder);
    }

    public void delete(Long id) {
        productOrderRepository.deleteById(id);
    }
}
