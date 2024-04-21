package com.example.demo.repository;

import com.example.demo.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findAllByVanIsNull();

    List<Delivery> findAllByVanIsNotNull();
}
