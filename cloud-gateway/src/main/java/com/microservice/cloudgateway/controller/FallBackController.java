package com.microservice.cloudgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class FallBackController {

    @GetMapping("/orderServiceFallBack")
    public String fallBackOrderService(){
        return "Order service is down";
    }

    @GetMapping("/productServiceFallBack")
    public String fallBackProductService(){
        return "Product service is down";
    }

    @GetMapping("/paymentServiceFallBack")
    public String fallBackPaymentService(){
        return "Payment service is down";
    }

}
