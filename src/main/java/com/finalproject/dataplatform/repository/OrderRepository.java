package com.finalproject.dataplatform.repository;

import com.finalproject.dataplatform.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    // logics
}
