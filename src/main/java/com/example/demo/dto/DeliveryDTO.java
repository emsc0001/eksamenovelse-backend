package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DeliveryDTO {
    private Long id;
    private LocalDate deliveryDate;
    private String destination;
    private String fromWarehouse;
    private Long vanId;
    private List<ProductOrderDTO> productOrders;
    private double totalPrice;
    private double totalWeightInKg;
}
