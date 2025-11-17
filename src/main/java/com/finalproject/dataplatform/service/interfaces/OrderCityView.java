package com.finalproject.dataplatform.service.interfaces;

import java.time.LocalDateTime;

public interface OrderCityView {
    String getOrderId();
    String getCustomerId();
    String getCustomerCity();
    Double getPrice();
    LocalDateTime getOrderPurchaseTimestamp();
    LocalDateTime getOrderDeliveredCustomerDate();
    String getProductCategoryNameEnglish();
}
