package com.example.demo.model.openFeign.imp;

import org.springframework.stereotype.Component;

import com.example.demo.controller.dto.ProductWebDto;
import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.openFeign.ProductClient;

@Component
public class ProductServiceFallback implements ProductClient {
    @Override
    public ProductWebDto getProductById(Long id) {
        // Safe fallback when Product Service is down
    	System.out.println("Product not found");
    	ProductWebDto fallback = new ProductWebDto();
        fallback.setId(id);
        fallback.setName("Unknown Product");
        fallback.setStock(0);
        return fallback;
    }
}
