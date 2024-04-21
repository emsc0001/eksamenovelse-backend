package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Van {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String model;
    private double capacityInKilograms;
    @OneToMany
    private List<Delivery> deliveries;

    public Van(String brand, String model, double capacityInKilograms) {
        this.brand = brand;
        this.model = model;
        this.capacityInKilograms = capacityInKilograms;
    }

    public boolean hasCapacityForDelivery(Delivery delivery) {
        if (delivery == null) return false;
        return deliveries
                .stream()
                .mapToInt(Delivery::getTotalWeightInKg)
                .sum() + delivery.getTotalWeightInKg() <= capacityInKilograms;
    }
}
