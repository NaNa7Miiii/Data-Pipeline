package com.example.demo.repository;

import com.example.demo.model.Customer;
import com.example.demo.service.interfaces.OrderCityView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    // logics
    // startdate, enddate, category,
    // startdate, enddate, category, customerid, customeruniqueid, price

    // two tables, one with date and one without, join
    @Query("""
        SELECT c.customerId as customerId,
               c.customerUniqueId as customerUniqueId,
               c.customerCity as customerCity,
               o.orderId as orderId,
               o.price as price,
               o.orderStatus as orderStatus,
               o.orderPurchaseTimestamp as orderPurchaseTimestamp,
               o.productCategoryNameEnglish as productCategoryNameEnglish,
               o.reviewScore as reviewScore
        FROM Customer c
        JOIN Order o ON c.customerId = o.customerId
        WHERE (o.orderPurchaseTimestamp >= COALESCE(:startDate, o.orderPurchaseTimestamp))
          AND (o.orderPurchaseTimestamp <= COALESCE(:endDate, o.orderPurchaseTimestamp))
          AND (:city = '' OR c.customerCity = COALESCE(:city, c.customerCity))
          AND (:category = '' OR o.productCategoryNameEnglish = COALESCE(:category, o.productCategoryNameEnglish))
    """)
    List<OrderCityView> findCustomersByFilters (
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("city") String city,
            @Param("category") String category
    );
//    @Query("""
//        SELECT c.customerId as customerId,
//        FROM Customer c
//        JOIN Order o ON c.customerId = o.customerId
//        WHERE (o.orderPurchaseTimestamp >= COALESCE(:startDate, o.orderPurchaseTimestamp))
//          AND (o.orderPurchaseTimestamp <= COALESCE(:endDate, o.orderPurchaseTimestamp))
//          AND (:city = '' OR c.customerCity = :city)
//          AND (:category = '' OR o.productCategoryNameEnglish = :category)
//    """)
//    List<OrderCityView> findCustomerHistory (
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate,
//            @Param("category") String category
//    );
}