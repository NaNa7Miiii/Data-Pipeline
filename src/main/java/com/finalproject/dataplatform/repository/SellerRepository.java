package com.finalproject.dataplatform.repository;

import com.finalproject.dataplatform.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
    // logics
}
