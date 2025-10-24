package com.example.demo.model.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dao.CustomerRepository;
import com.example.demo.model.dao.entity.Customer;
import com.example.demo.model.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    public Customer saveCustomer(Customer product) {
        return customerRepository.save(product);
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public void deleteCustomer(Long id) {
    	customerRepository.deleteById(id);
    }
    
    public Customer updateCustomer(Customer inputedCustomer, Long id) {
    	Optional<Customer> tobeUpdated = customerRepository.findById(id);
    	return tobeUpdated.map(customer -> {
    		customer.setFirstName(inputedCustomer.getFirstName());
    		customer.setLastName(inputedCustomer.getLastName());
    		customer.setEmail(inputedCustomer.getEmail());
    		customer.setPhoneNumber(inputedCustomer.getPhoneNumber());
    		customer.setAddress(inputedCustomer.getAddress());
    		return customerRepository.save(customer);
    	}).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    	
    }
}
