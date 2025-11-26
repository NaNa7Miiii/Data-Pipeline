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

    // 使用 CAST(... as timestamp) 和 CAST(... as string) 解决 Postgres 参数类型推断问题
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
        WHERE (o.orderPurchaseTimestamp >= COALESCE(CAST(:startDate AS timestamp), o.orderPurchaseTimestamp))
          AND (o.orderPurchaseTimestamp <= COALESCE(CAST(:endDate AS timestamp), o.orderPurchaseTimestamp))
          AND (c.customerCity = COALESCE(CAST(:city AS string), c.customerCity))
          AND (o.productCategoryNameEnglish = COALESCE(CAST(:category AS string), o.productCategoryNameEnglish))
    """)
    List<OrderCityView> findOrdersByFilters (
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("city") String city,
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