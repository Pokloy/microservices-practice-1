package com.example.demo.model.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.model.dao.entity.Order;
import com.example.demo.model.dto.OrderDto;

public interface OrderService {
	public List<OrderDto> getAllOrders();
	
	public OrderDto createOrder(Order order);
	
	public Optional<OrderDto> getOrderById(Long id);
	
	public void deleteOrder(Long id);
	
	public OrderDto updateOrderStatus(String status, Long id);
}
