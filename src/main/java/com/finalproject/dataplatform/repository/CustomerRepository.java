package com.finalproject.dataplatform.repository;

import com.finalproject.dataplatform.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    // logics
}
