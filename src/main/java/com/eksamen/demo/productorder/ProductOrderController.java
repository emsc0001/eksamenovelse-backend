// ProductOrderController.java
package com.eksamen.demo.productorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product-orders")
public class ProductOrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @GetMapping
    public List<ProductOrder> getAllProductOrders() {
        return productOrderService.getAllProductOrders();
    }

    // Add more controller methods as needed
}