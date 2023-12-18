package com.microservice.productservice.service;

import com.microservice.productservice.entity.Product;
import com.microservice.productservice.exception.ProductServiceCustomException;
import com.microservice.productservice.model.ProductRequest;
import com.microservice.productservice.model.ProductResponse;
import com.microservice.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    @Override
    public Product addProduct(ProductRequest productRequest) {

        log.info("Create product");
        Product product = Product.builder()
                .productName(productRequest.name())
                .price(productRequest.price())
                .quantity(productRequest.quantity())
                .build();
        product = productRepository.save(product);
        log.info("Product created successfully");
        return product;
    }

    @Override
    public ProductResponse getProductById(long id) {

        log.info("Get product with id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductServiceCustomException("Product not found", "PRODUCT_NOT_FOUND"));

        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);

        log.info("Get product successfully");

        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce quantity for product id: {}", productId);

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductServiceCustomException("Product not found", "PRODUCT_NOT_FOUND")
        );
        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException("Product does not have sufficient quantity", "INSUFFICIENT_QUANTITY");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        log.info("Product quantity updated successfully");
    }


}
