package com.example.demo.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.dao.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
