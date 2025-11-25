package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.service.interfaces.OrderCityView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderRepository extends JpaRepository<Order, String> {
    @Query("""
        SELECT o.orderId as orderId,
               o.customerId as customerId,
               c.customerCity as customerCity,
               o.price as price,
               o.orderPurchaseTimestamp as orderPurchaseTimestamp,
               o.orderDeliveredCustomerDate as orderDeliveredCustomerDate,
               o.productCategoryNameEnglish as productCategoryNameEnglish
        FROM Order o
        JOIN Customer c ON o.customerId = c.customerId
        WHERE (o.orderPurchaseTimestamp >= COALESCE(:startDate, o.orderPurchaseTimestamp))
          AND (o.orderPurchaseTimestamp <= COALESCE(:endDate, o.orderPurchaseTimestamp))
          AND (c.customerCity = COALESCE(:city, c.customerCity))
          AND (o.productCategoryNameEnglish = COALESCE(:category, o.productCategoryNameEnglish))
    """)
    List<OrderCityView> findOrdersByFilters (
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("  city") String city,
            @Param("category") String category
    );
    @Query("SELECT o.orderStatus FROM Order o WHERE o.orderId = :orderId")
    String findStatusById(@Param("orderId") String orderId);

    @Query(value = """
        SELECT seller_id, SUM(price) as total_sales
        FROM orders
        WHERE order_purchase_timestamp BETWEEN :startDate AND :endDate
        GROUP BY seller_id
        ORDER BY total_sales DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Map<String, Object>> findTopSellers(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("limit") int limit
    );
}
