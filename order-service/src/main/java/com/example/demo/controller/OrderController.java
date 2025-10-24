package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controller.dto.StatusWebDto;
import com.example.demo.model.dao.entity.Order;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@GetMapping
	public List<OrderDto> getAll(){
		return orderService.getAllOrders();
	}
	
	@PostMapping
	public OrderDto create(@RequestBody Order order) {
		return orderService.createOrder(order);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OrderDto> getById(@PathVariable Long id){
		return orderService.getOrderById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());			
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		orderService.deleteOrder(id);
	}
	
	@PutMapping("/{id}/status")
	public OrderDto updateOrder(
			@RequestBody StatusWebDto status,
			@PathVariable Long id) {
		
		return orderService.updateOrderStatus(status.getStatus(), id);
	}
}
