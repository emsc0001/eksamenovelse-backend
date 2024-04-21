package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    @ManyToOne
    private Product product;

    public ProductOrder(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public double getTotalPrice(){
        return quantity * product.getPrice();
    }

    public int getTotalWeightInGrams(){
        return quantity * product.getWeightInGrams();
    }
}
