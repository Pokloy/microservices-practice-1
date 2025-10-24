package com.example.demo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.dao.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
