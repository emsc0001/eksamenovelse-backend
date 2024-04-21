package com.example.demo.controller;

import com.example.demo.dto.DeliveryDTO;
import com.example.demo.dto.ProductOrderDTO;
import com.example.demo.dto.VanDTO;
import com.example.demo.errorhandling.exception.NotFoundException;
import com.example.demo.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {
    DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAll(@RequestParam(required = false) Boolean assignedToVan) {
        var deliveries = deliveryService.getAll(assignedToVan);
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getById(@PathVariable Long id) {
        var delivery = deliveryService.getById(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(delivery);
    }

    @PostMapping
    public ResponseEntity<DeliveryDTO> save(@RequestBody DeliveryDTO deliveryDTO) {
        var delivery = deliveryService.save(deliveryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(delivery);
    }

    @PutMapping("/{id}/van")
    public ResponseEntity<DeliveryDTO> updateVan(@PathVariable Long id, @RequestBody VanDTO van) {
        var delivery = deliveryService.assignVan(id, van);
        return ResponseEntity.ok(delivery);
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<ProductOrderDTO>> getProductOrders(@PathVariable Long id) {
        var productOrders = deliveryService.getProductOrders(id);
        return ResponseEntity.ok(productOrders);
    }

    @PostMapping("/{id}/orders")
    public ResponseEntity<DeliveryDTO> addProductOrders(@PathVariable Long id, @RequestBody List<ProductOrderDTO> productOrderDTOS) {
        var delivery = deliveryService.addProductOrders(id, productOrderDTOS);
        return ResponseEntity.ok(delivery);
    }

    @DeleteMapping("/{id}/orders/{productOrderId}")
    public ResponseEntity<DeliveryDTO> removeProductOrder(@PathVariable Long id, @PathVariable Long productOrderId) {
        var delivery = deliveryService.removeProductOrder(id, productOrderId);
        return ResponseEntity.ok(delivery);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
