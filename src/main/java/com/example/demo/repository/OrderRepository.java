package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.service.interfaces.OrderCityView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("""
        SELECT o.orderId as orderId,
               o.customerId as customerId,
               c.customerCity as customerCity,
               o.price as price,
               o.orderStatus as orderStatus,
               o.orderPurchaseTimestamp as orderPurchaseTimestamp,
               o.orderApprovedAt as orderApprovedAt,
               o.orderDeliveredCarrierDate as orderDeliveredCarrierDate,
               o.orderDeliveredCustomerDate as orderDeliveredCustomerDate,
               o.orderEstimatedDeliveryDate as orderEstimatedDeliveryDate,
               o.productCategoryNameEnglish as productCategoryNameEnglish,
               o.reviewScore as reviewScore
        FROM Order o
        JOIN Customer c ON o.customerId = c.customerId
        WHERE (o.orderPurchaseTimestamp >= COALESCE(:startDate, o.orderPurchaseTimestamp))
          AND (o.orderPurchaseTimestamp <= COALESCE(:endDate, o.orderPurchaseTimestamp))
          AND (:city = '' OR c.customerCity = COALESCE(:city, c.customerCity))
          AND (:category = '' OR o.productCategoryNameEnglish = COALESCE(:category, o.productCategoryNameEnglish))
    """)
    List<OrderCityView> findOrdersByFilters (
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("city") String city,
            @Param("category") String category
    );
}