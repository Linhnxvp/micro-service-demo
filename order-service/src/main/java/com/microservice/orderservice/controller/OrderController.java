package com.microservice.orderservice.controller;

import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.model.OrderResponse;
import com.microservice.orderservice.model.OrderRequest;
import com.microservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest){
        Order order = orderService.placeOrder(orderRequest);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable long orderId){
        OrderResponse orderResponse = orderService.getOrderById(orderId);

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

}
