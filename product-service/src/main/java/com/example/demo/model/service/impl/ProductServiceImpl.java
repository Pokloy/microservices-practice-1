package com.example.demo.model.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.model.dao.ProductRepository;
import com.example.demo.model.dao.entity.Product;
import com.example.demo.model.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    public Product updateProduct(Product inputedProduct, Long id) {
    	Optional<Product> tobeUpdated = productRepository.findById(id);
    	return tobeUpdated.map(product -> {
    		product.setId(id);
    		product.setName(inputedProduct.getName());
    		product.setPrice(inputedProduct.getPrice());
    		product.setStock(inputedProduct.getStock());
    		return productRepository.saveAndFlush(inputedProduct);
    	}).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    	
    }
}
