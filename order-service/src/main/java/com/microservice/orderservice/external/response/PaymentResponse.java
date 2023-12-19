package com.microservice.orderservice.external.response;

import com.microservice.orderservice.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private long paymentId;
    private long orderId;
    private String status;
    private PaymentMode paymentMode;
    private long amount;
    private Instant paymentDate;
}
