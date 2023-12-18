package com.microservice.orderservice.service;

import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.model.OrderRequest;

public interface OrderService {
    Order placeOrder(OrderRequest orderRequest);
}
