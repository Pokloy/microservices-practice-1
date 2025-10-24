package com.example.demo.model.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.dao.entity.Customer;

public interface CustomerService {
	public List<Customer> getAllCustomers();
	
	public Customer saveCustomer(Customer product);
	
	public Optional<Customer> getCustomerById(Long id);
	
	public void deleteCustomer(Long id);
	 
	public Customer updateCustomer(Customer inputedProduct, Long id);
}	
