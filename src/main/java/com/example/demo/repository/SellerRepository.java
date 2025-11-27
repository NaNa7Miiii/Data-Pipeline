package com.example.demo.repository;

import com.example.demo.model.Seller;
import com.example.demo.service.interfaces.OrderCityView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
    // logics
    @Query("""
        SELECT s.sellerId as sellerId,
               s.sellerCity as sellerCity,
               o.orderId as orderId,
               o.price as price,
               o.orderStatus as orderStatus,
               o.orderPurchaseTimestamp as orderPurchaseTimestamp,
               o.productCategoryNameEnglish as productCategoryNameEnglish,
               o.reviewScore as reviewScore
        FROM Seller s
        JOIN Order o ON s.sellerId = o.sellerId
        WHERE (o.orderPurchaseTimestamp >= COALESCE(:startDate, o.orderPurchaseTimestamp))
          AND (o.orderPurchaseTimestamp <= COALESCE(:endDate, o.orderPurchaseTimestamp))
          AND (:city = '' OR s.sellerCity = COALESCE(:city, s.sellerCity))
          AND (:category = '' OR o.productCategoryNameEnglish = COALESCE(:category, o.productCategoryNameEnglish))
    """)
    List<OrderCityView> findSellersByFilters (
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("city") String city,
            @Param("category") String category
    );
}
