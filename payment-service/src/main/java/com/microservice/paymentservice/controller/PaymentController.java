package com.microservice.paymentservice.controller;

import com.microservice.paymentservice.model.PaymentRequest;
import com.microservice.paymentservice.model.PaymentResponse;
import com.microservice.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest){
        return new ResponseEntity<>(paymentService.doPayment(paymentRequest), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetails(@PathVariable long orderId){
        PaymentResponse paymentResponse = paymentService.getPaymentDetailsByOrderId(orderId);

        return new ResponseEntity<>(paymentResponse,HttpStatus.OK);
    }
}
