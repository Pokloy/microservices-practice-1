package com.example.demo.model.service.circuitBreaker;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import com.example.demo.controller.dto.ProductWebDto;
import com.example.demo.model.openFeign.ProductClient;

@Service
public class ProductServiceCB {

    private static final String PRODUCT_SERVICE_CB = "productServiceCB";

    private final ProductClient productClient;

    public ProductServiceCB(ProductClient productClient) {
        this.productClient = productClient;
    }

    @CircuitBreaker(name = PRODUCT_SERVICE_CB, fallbackMethod = "fallbackGetProduct")
    public ProductWebDto getProductById(Long id) {
        // This call is protected by Resilience4j
        return productClient.getProductById(id);
    }

    public ProductWebDto fallbackGetProduct(Long id, Throwable ex) {
        System.out.println("Product not found");
    	ProductWebDto fallback = new ProductWebDto();
        fallback.setId(id);
        fallback.setName("Unavailable Product");
        fallback.setPrice(new BigDecimal(0));
        fallback.setStock(0);
        return fallback;
    }
}
