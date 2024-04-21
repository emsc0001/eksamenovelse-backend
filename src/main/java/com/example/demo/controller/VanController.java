package com.example.demo.controller;

import com.example.demo.dto.DeliveryDTO;
import com.example.demo.dto.VanDTO;
import com.example.demo.errorhandling.exception.NotFoundException;
import com.example.demo.service.VanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vans")
public class VanController {

    VanService vanService;

    public VanController(VanService vanService) {
        this.vanService = vanService;
    }

    @GetMapping
    public ResponseEntity<List<VanDTO>> getAll() {
        var vans = vanService.getAll();
        return ResponseEntity.ok(vans);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VanDTO> getById(@PathVariable Long id) {
        var van = vanService.getById(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(van);
    }

    @PostMapping
    public ResponseEntity<VanDTO> save(@RequestBody VanDTO vanDTO) {
        var van = vanService.save(vanDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(van);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VanDTO> update(@PathVariable Long id, @RequestBody VanDTO vanDTO) {
        var van = vanService.update(id, vanDTO);
        return ResponseEntity.ok(van);
    }

    @PostMapping("/{id}/deliveries")
    public ResponseEntity<VanDTO> addDelivery(@PathVariable Long id, @RequestBody DeliveryDTO deliveryDTO) {
        var van = vanService.addDelivery(id, deliveryDTO);
        return ResponseEntity.ok(van);
    }

    @GetMapping("/{id}/deliveries")
    public ResponseEntity<List<DeliveryDTO>> getDeliveries(@PathVariable Long id) {
        var deliveries = vanService.getDeliveries(id);
        return ResponseEntity.ok(deliveries);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
