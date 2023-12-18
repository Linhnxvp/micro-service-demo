package com.microservice.productservice.service;

import com.microservice.productservice.entity.Product;
import com.microservice.productservice.model.ProductRequest;
import com.microservice.productservice.model.ProductResponse;

public interface ProductService {
    Product addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long id);

    void reduceQuantity(long productId, long quantity);
}
