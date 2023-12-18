package com.microservice.orderservice.model;

public record OrderRequest(long productId, long totalAmount, long quantity, PaymentMode paymentMode) {
}
