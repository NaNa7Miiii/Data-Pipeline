package com.finalproject.dataplatform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @Column(name = "order_id", length=64)
    private String orderId;

    @Column(name = "customer_id", length=64)
    private String customerId;

    @Column(name = "order_status", length=32)
    private String orderStatus;

    @Column(name = "order_purchase_timestamp")
    private LocalDateTime orderPurchaseTimestamp;

    @Column(name = "order_approved_at")
    private LocalDateTime orderApprovedAt;

    @Column(name = "order_delivered_carrier_date")
    private LocalDateTime orderDeliveredCarrierDate;

    @Column(name = "order_delivered_customer_date")
    private LocalDateTime orderDeliveredCustomerDate;

    @Column(name = "order_estimated_delivery_date")
    private LocalDateTime orderEstimatedDeliveryDate;

    @Column(name = "order_item_id")
    private Double orderItemId;

    @Column(name = "product_id", length=64)
    private String productId;

    @Column(name = "seller_id", length=64)
    private String sellerId;

    @Column(name = "shipping_limit_date")
    private LocalDateTime shippingLimitDate;

    @Column(name = "price")
    private Double price;

    @Column(name = "freight_value")
    private Double freightValue;

    @Column(name = "payment_sequential")
    private Double paymentSequential;

    @Column(name = "payment_type", length = 32)
    private String paymentType;

    @Column(name = "payment_installments")
    private Double paymentInstallments;

    @Column(name = "payment_value")
    private Double paymentValue;

    @Column(name = "review_id", length = 64)
    private String reviewId;

    @Column(name = "review_score")
    private Double reviewScore;

    @Column(name = "review_comment_title", length = 255)
    private String reviewCommentTitle;

    @Column(name = "review_comment_message", length = 2000)
    private String reviewCommentMessage;

    @Column(name = "review_creation_date")
    private LocalDateTime reviewCreationDate;

    @Column(name = "review_answer_timestamp")
    private LocalDateTime reviewAnswerTimestamp;

    @Column(name = "product_category_name", length = 128)
    private String productCategoryName;

    @Column(name = "product_name_length")
    private Double productNameLength;

    @Column(name = "product_description_length")
    private Double productDescriptionLength;

    @Column(name = "product_photos_qty")
    private Double productPhotosQty;

    @Column(name = "product_weight_g")
    private Double productWeightG;

    @Column(name = "product_length_cm")
    private Double productLengthCm;

    @Column(name = "product_height_cm")
    private Double productHeightCm;

    @Column(name = "product_width_cm")
    private Double productWidthCm;

    @Column(name = "product_category_name_english", length = 128)
    private String productCategoryNameEnglish;
}
