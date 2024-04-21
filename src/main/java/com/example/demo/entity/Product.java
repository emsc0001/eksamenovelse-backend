package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private int weightInGrams;
    private Boolean available = true;

    public Product(String name, double price, int weightInGrams, boolean available) {
        this.name = name;
        this.price = price;
        this.weightInGrams = weightInGrams;
        this.available = available;
    }
}
