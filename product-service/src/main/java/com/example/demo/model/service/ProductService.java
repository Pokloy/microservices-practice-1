package com.example.demo.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.dao.ProductRepository;
import com.example.demo.model.dao.entity.Product;


public interface ProductService {
	public List<Product> getAllProducts();
	
	public Product saveProduct(Product product);
	
	public Optional<Product> getProductById(Long id);
	
	public void deleteProduct(Long id);
	 
	public Product updateProduct(Product inputedProduct, Long id);
}
