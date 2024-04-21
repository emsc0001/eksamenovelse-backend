package com.example.demo.config;

import com.example.demo.entity.Delivery;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductOrder;
import com.example.demo.entity.Van;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.DeliveryService;
import com.example.demo.service.ProductOrderService;
import com.example.demo.service.VanService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitData implements CommandLineRunner {

    ProductRepository productRepository;
    VanService vanService;
    ProductOrderService productOrderService;
    DeliveryService deliveryService;

    public InitData(ProductRepository productRepository, VanService vanService, ProductOrderService productOrderService, DeliveryService deliveryService) {
        this.productRepository = productRepository;
        this.vanService = vanService;
        this.productOrderService = productOrderService;
        this.deliveryService = deliveryService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing data...");
        if (productRepository.count() == 0) {
            createData();
        }
    }

    public void createData() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Carlsberg Sport", 15.0, 500, true));
        products.add(new Product("Coca Cola", 15.0, 500, true));
        products.add(new Product("Pepsi", 15.0, 500, true));
        products.add(new Product("Fanta", 15.0, 500, true));
        products.add(new Product("Sprite", 15.0, 500, true));
        products.add(new Product("Red Bull", 20.0, 250, true));
        products.add(new Product("Haribo Matador Mix", 36.0, 325, true));
        products.add(new Product("Haribo Stjerne Mix", 36.0, 325, true));
        products.add(new Product("Le Arche Ripasso", 50.0, 750, true));
        products.add(new Product("Selaks, Baron de Ley", 50.0, 750, true));
        products.add(new Product("Chateau de la Gardine", 75.0, 750, true));

        productRepository.saveAll(products);

        List<ProductOrder> orders = new ArrayList<>();
        orders.add(new ProductOrder(2, products.get(0)));
        orders.add(new ProductOrder(3, products.get(1)));
        orders.add(new ProductOrder(1, products.get(2)));
        orders.add(new ProductOrder(5, products.get(3)));
        orders.add(new ProductOrder(2, products.get(4)));
        orders.add(new ProductOrder(1, products.get(5)));
        orders.add(new ProductOrder(3, products.get(5)));
        orders.add(new ProductOrder(1, products.get(6)));
        orders.add(new ProductOrder(2, products.get(6)));
        orders.add(new ProductOrder(1, products.get(7)));
        orders.add(new ProductOrder(2, products.get(7)));
        orders.add(new ProductOrder(1, products.get(8)));
        orders.add(new ProductOrder(2, products.get(8)));
        orders.add(new ProductOrder(1, products.get(9)));
        orders.add(new ProductOrder(2, products.get(9)));
        orders.add(new ProductOrder(1, products.get(10)));
        orders.add(new ProductOrder(2, products.get(10)));

        orders
                .stream()
                .map(productOrderService::toDTO)
                .forEach(productOrderService::save);

        List<Van> vans = new ArrayList<>();
        vans.add(new Van("Ford", "Transit", 1000));
        vans.add(new Van("Ford", "Transit", 1000));
        vans.add(new Van("Ford", "Transit", 1000));

        vans
                .stream()
                .map(vanService::toDTO)
                .forEach(vanService::save);

        var newOrders = productOrderService.getAll().stream().map(productOrderService::toEntity).toList();

        List<Delivery> deliveries = new ArrayList<>();
        deliveries.add(new Delivery(java.time.LocalDate.now(), "Ninna Jørgensen, Nørrebrogade 23, 2 tv, 2200 Nørrebro", "København", newOrders.subList(0, 5)));
        deliveries.add(new Delivery(java.time.LocalDate.now(), "Thomas Jensen, Roskildevej 105, st mf, 2000 Frederiksberg", "København", newOrders.subList(5, 10)));
        deliveries.add(new Delivery(java.time.LocalDate.now(), "Lars Larsen, Hovedgaden 1, 1 th, 1000 København K", "København", newOrders.subList(10, 15)));
        deliveries.add(new Delivery(java.time.LocalDate.now(), "Lars Larsen, Magsvej 1, 9000 Aalborg", "Aalborg", newOrders.subList(15, 17)));

        deliveries
                .stream()
                .map(deliveryService::toDTO)
                .forEach(deliveryService::save);
    }
}
