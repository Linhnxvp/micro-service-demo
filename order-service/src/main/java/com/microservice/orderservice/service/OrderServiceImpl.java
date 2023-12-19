package com.microservice.orderservice.service;

import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.exception.CustomException;
import com.microservice.orderservice.external.client.PaymentService;
import com.microservice.orderservice.external.client.ProductService;
import com.microservice.orderservice.external.request.PaymentRequest;
import com.microservice.orderservice.external.response.PaymentResponse;
import com.microservice.orderservice.external.response.ProductResponse;
import com.microservice.orderservice.model.OrderResponse;
import com.microservice.orderservice.model.OrderRequest;
import com.microservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final ProductService productService;

    private final PaymentService paymentService;

    private final RestTemplate restTemplate;

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
        orderRepository.save(order);

        log.info("Calling Payment service");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getOrderId())
                .amount(orderRequest.totalAmount())
                .paymentMode(orderRequest.paymentMode())
                .build();

        String orderStatus = null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully");
            orderStatus = "OK";
        }catch (Exception e){
            log.info("Error occurred in payment");
            orderStatus = "FAILED";
        }

        order.setOrderStatus(orderStatus);
        order = orderRepository.save(order);

        log.info("Order created successfully");

        return order;
    }

    @Override
    public OrderResponse getOrderById(long orderId) {

        log.info("Get order details by Id: {}", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException("Not found Order", "NOT_FOUND_ORDER", 404)
        );

        log.info("Invoking product service to fetch data product");
        ProductResponse productResponse = restTemplate.getForObject(
                "http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductResponse.class
        );

        log.info("Invoking payment service to fetch data payment");
        PaymentResponse paymentResponse = restTemplate.getForObject(
                "http://PAYMENT-SERVICE/payment/" + orderId, PaymentResponse.class
        );

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productId(productResponse.getProductId())
                .productName(productResponse.getProductName())
                .price(productResponse.getPrice())
                .quantity(productResponse.getQuantity())
                .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .orderId(paymentResponse.getOrderId())
                .status(paymentResponse.getStatus())
                .amount(paymentResponse.getAmount())
                .paymentMode(paymentResponse.getPaymentMode())
                .paymentDate(paymentResponse.getPaymentDate())
                .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getOrderId())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();


        return orderResponse;
    }
}
