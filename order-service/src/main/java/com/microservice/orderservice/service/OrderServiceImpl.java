package com.microservice.orderservice.service;

import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.external.client.ProductService;
import com.microservice.orderservice.model.OrderRequest;
import com.microservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final ProductService productService;
    @Override
    public Order placeOrder(OrderRequest orderRequest) {

        log.info("Create Order with product id: {}", orderRequest.productId());

        productService.reduceQuantity(orderRequest.productId(), orderRequest.quantity());

        Order order = Order.builder()
                .productId(orderRequest.productId())
                .amount(orderRequest.totalAmount())
                .quantity(orderRequest.quantity())
                .orderStatus("CREATED")
                .orderDate(Instant.now())
                .build();
        order = orderRepository.save(order);
        log.info("Order created successfully");

        return order;
    }
}
