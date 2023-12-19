package com.microservice.paymentservice.model;

public record PaymentRequest(long orderId, long amount, String referenceNumber, PaymentMode paymentMode) {
}
