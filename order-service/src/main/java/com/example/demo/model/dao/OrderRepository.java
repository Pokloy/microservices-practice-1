package com.example.demo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.dao.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

