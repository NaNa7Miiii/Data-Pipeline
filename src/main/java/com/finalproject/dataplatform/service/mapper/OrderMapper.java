package com.finalproject.dataplatform.service.mapper;
import com.finalproject.dataplatform.dto.requests.OrderRequestDto;
import com.finalproject.dataplatform.dto.responses.OrderResponseDto;
import com.finalproject.dataplatform.model.Order;

public class OrderMapper {

    // Entity -> ResponseDto
    public static OrderResponseDto toResponseDto(Order entity) {
        if (entity == null) return null;

        return new OrderResponseDto(
                entity.getOrderId(),
                entity.getCustomerId(),
                entity.getOrderStatus(),
                entity.getOrderPurchaseTimestamp(),
                entity.getOrderApprovedAt(),
                entity.getOrderDeliveredCarrierDate(),
                entity.getOrderDeliveredCustomerDate(),
                entity.getOrderEstimatedDeliveryDate(),
                entity.getOrderItemId(),
                entity.getProductId(),
                entity.getSellerId(),
                entity.getShippingLimitDate(),
                entity.getPrice(),
                entity.getFreightValue(),
                entity.getPaymentSequential(),
                entity.getPaymentType(),
                entity.getPaymentInstallments(),
                entity.getPaymentValue(),
                entity.getReviewId(),
                entity.getReviewScore(),
                entity.getReviewCommentTitle(),
                entity.getReviewCommentMessage(),
                entity.getReviewCreationDate(),
                entity.getReviewAnswerTimestamp(),
                entity.getProductCategoryName(),
                entity.getProductNameLength(),
                entity.getProductDescriptionLength(),
                entity.getProductPhotosQty(),
                entity.getProductWeightG(),
                entity.getProductLengthCm(),
                entity.getProductHeightCm(),
                entity.getProductWidthCm(),
                entity.getProductCategoryNameEnglish()
        );
    }

    // RequestDto -> Entity
    public static Order toEntity(OrderRequestDto dto) {
        if (dto == null) return null;

        Order entity = new Order();
        entity.setOrderId(dto.orderId());
        entity.setCustomerId(dto.customerId());
        entity.setOrderStatus(dto.orderStatus());
        entity.setOrderPurchaseTimestamp(dto.orderPurchaseTimestamp());
        entity.setOrderApprovedAt(dto.orderApprovedAt());
        entity.setOrderDeliveredCarrierDate(dto.orderDeliveredCarrierDate());
        entity.setOrderDeliveredCustomerDate(dto.orderDeliveredCustomerDate());
        entity.setOrderEstimatedDeliveryDate(dto.orderEstimatedDeliveryDate());
        entity.setOrderItemId(dto.orderItemId());
        entity.setProductId(dto.productId());
        entity.setSellerId(dto.sellerId());
        entity.setShippingLimitDate(dto.shippingLimitDate());
        entity.setPrice(dto.price());
        entity.setFreightValue(dto.freightValue());
        entity.setPaymentSequential(dto.paymentSequential());
        entity.setPaymentType(dto.paymentType());
        entity.setPaymentInstallments(dto.paymentInstallments());
        entity.setPaymentValue(dto.paymentValue());
        entity.setReviewId(dto.reviewId());
        entity.setReviewScore(dto.reviewScore());
        entity.setReviewCommentTitle(dto.reviewCommentTitle());
        entity.setReviewCommentMessage(dto.reviewCommentMessage());
        entity.setReviewCreationDate(dto.reviewCreationDate());
        entity.setReviewAnswerTimestamp(dto.reviewAnswerTimestamp());
        entity.setProductCategoryName(dto.productCategoryName());
        entity.setProductNameLength(dto.productNameLength());
        entity.setProductDescriptionLength(dto.productDescriptionLength());
        entity.setProductPhotosQty(dto.productPhotosQty());
        entity.setProductWeightG(dto.productWeightG());
        entity.setProductLengthCm(dto.productLengthCm());
        entity.setProductHeightCm(dto.productHeightCm());
        entity.setProductWidthCm(dto.productWidthCm());
        entity.setProductCategoryNameEnglish(dto.productCategoryNameEnglish());

        return entity;
    }
}

