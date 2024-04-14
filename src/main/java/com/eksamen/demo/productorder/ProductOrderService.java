// ProductOrderService.java
package com.eksamen.demo.productorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOrderService {

    @Autowired
    private ProductOrderRepository productOrderRepository;

    public List<ProductOrder> getAllProductOrders() {
        return productOrderRepository.findAll();
    }

    // Add more service methods as needed
}