package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate deliveryDate;
    private String destination;
    private String fromWarehouse;
    @OneToMany
    private List<ProductOrder> productOrders;
    @ManyToOne
    private Van van;

    public Delivery(LocalDate deliveryDate, String destination, String fromWarehouse, List<ProductOrder> productOrders) {
        this.deliveryDate = deliveryDate;
        this.destination = destination;
        this.fromWarehouse = fromWarehouse;
        this.productOrders = productOrders;
    }

    public boolean hasCapacityForProductOrders(List<ProductOrder> productOrders) {
        if (van == null) return true;
        int productOrdersCombinedWeight = 0;
        for (var order : productOrders) {
            productOrdersCombinedWeight += (int)Math.ceil(order.getTotalWeightInGrams()/1000.0);
        }
        return getTotalWeightInKg() + productOrdersCombinedWeight <= van.getCapacityInKilograms();
    }

    public double getTotalPrice(){
        return productOrders
                .stream()
                .mapToDouble(ProductOrder::getTotalPrice)
                .sum();
    }

    public int getTotalWeightInKg(){
        return productOrders
                .stream()
                .mapToInt(p -> (int)Math.ceil(p.getTotalWeightInGrams() / 1000.0))
                .sum();
    }
}
