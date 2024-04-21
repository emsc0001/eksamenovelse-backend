package com.example.demo.repository;

import com.example.demo.entity.Van;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VanRepository extends JpaRepository<Van, Long> {
}
