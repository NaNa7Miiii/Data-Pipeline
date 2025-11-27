package com.example.demo.service.interfaces;

import java.time.LocalDateTime;

public interface OrderCityView {
    String getCustomerId();
    String getUniqueCustomerId();
    String getCustomerCity();

    String getOrderId();
    Double getPrice();
    String getOrderStatus();
    LocalDateTime getOrderPurchaseTimestamp();
    LocalDateTime getOrderApprovedAt();
    LocalDateTime getOrderDeliveredCarrierDate();
    LocalDateTime getOrderDeliveredCustomerDate();
    LocalDateTime getOrderEstimatedDeliveryDate();
    Double getReviewScore();
    String getProductCategoryNameEnglish();

    String getSellerId();
    String getSellerCity();
}
