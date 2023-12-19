package com.microservice.paymentservice.service;

import com.microservice.paymentservice.entity.Payment;
import com.microservice.paymentservice.exception.CustomException;
import com.microservice.paymentservice.model.PaymentMode;
import com.microservice.paymentservice.model.PaymentRequest;
import com.microservice.paymentservice.model.PaymentResponse;
import com.microservice.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;

    @Override
    public Long doPayment(PaymentRequest paymentRequest) {

        log.info("Create Payment");
        Payment payment = Payment.builder()
                .orderId(paymentRequest.orderId())
                .paymentMode(paymentRequest.paymentMode().name())
                .paymentDate(Instant.now())
                .amount(paymentRequest.amount())
                .paymentStatus("SUCCESS")
                .referenceNumber(paymentRequest.referenceNumber())
                .build();

        paymentRepository.save(payment);

        log.info("Payment created successfully");

        return payment.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsByOrderId(long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new CustomException("Not found Payment", "NOT_FOUND_PAYMENT")
        );

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .paymentMode(PaymentMode.valueOf(payment.getPaymentMode()))
                .status(payment.getPaymentStatus())
                .paymentDate(payment.getPaymentDate())
                .amount(payment.getAmount())
                .build();

        return paymentResponse;
    }
}
