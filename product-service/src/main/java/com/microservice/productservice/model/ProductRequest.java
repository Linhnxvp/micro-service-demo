package com.microservice.productservice.model;

public record ProductRequest(String name, long price, long quantity) {
}
