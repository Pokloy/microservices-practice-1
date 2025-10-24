package com.example.demo.model.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.controller.dto.CustomerWebDto;
import com.example.demo.controller.dto.ProductWebDto;
import com.example.demo.model.dao.OrderRepository;
import com.example.demo.model.dao.entity.Order;
import com.example.demo.model.dao.entity.OrderItem;
import com.example.demo.model.dto.OrderDto;
import com.example.demo.model.kafka.KafkaProducerService;
import com.example.demo.model.mapper.OrderMapper;
import com.example.demo.model.obj.OrderItemObj;
import com.example.demo.model.openFeign.CustomerClient;
import com.example.demo.model.openFeign.ProductClient;
import com.example.demo.model.service.OrderService;
import com.example.demo.model.service.circuitBreaker.ProductServiceCB;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private ProductServiceCB productServiceCB;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private CustomerClient customerClient;
	
	@Autowired
	private KafkaProducerService kafkaProducerService;

	
	@Transactional
	public OrderDto createOrder(Order order) {
	    // 1. Validate customer
	    CustomerWebDto customer = customerClient.getCustomerById(order.getCustomerId());
	    if (customer == null) {
	        throw new RuntimeException("Invalid customer ID: " + order.getCustomerId());
	    }

	    // 2. Validate products
	    for (OrderItem item : order.getItems()) {
	        ProductWebDto product = productServiceCB.getProductById(item.getProductId());
	        if (product == null) {
	            throw new RuntimeException("Invalid product ID: " + item.getProductId());
	        }

	        item.setPrice(product.getPrice());
	        item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf((item.getQuantity() == 0) ? item.getQuantity() : 0)));
	        item.setOrder(order);
	    }

	    // 3. Metadata
	    order.setOrderDate(LocalDateTime.now());
	    order.setStatus("Pending");

	    // 4. Compute total
	    BigDecimal total = order.getItems().stream()
	        .map(i -> i.getSubtotal())
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	    order.setTotalAmount(total);

	    // 5. Save and publish Kafka event
	    Order savedOrder = orderRepository.save(order);
	    
	    OrderDto orderDto = new OrderDto();
	    
	    List<OrderItemObj> orderI = new ArrayList<>();
	    
	    for(OrderItem orderItem: savedOrder.getItems()) {
	    	OrderItemObj orderIt = new OrderItemObj();
	    	orderIt.setId(orderItem.getId());
	    	orderIt.setProductId(orderItem.getProductId());
	    	orderIt.setQuantity(orderItem.getQuantity());
	    	orderIt.setPrice(orderItem.getPrice());
	    	orderIt.setSubtotal(orderItem.getSubtotal());
	    	orderI.add(orderIt);
	    }
	    
	    orderDto.setId(savedOrder.getId());
	    orderDto.setCustomerId(savedOrder.getCustomerId());
	    orderDto.setOrderDate(savedOrder.getOrderDate());
	    orderDto.setStatus(savedOrder.getStatus());
	    orderDto.setTotalAmount(total);
	    orderDto.setItems(orderI);
	    
	    // 6. Send Kafka event ✅
	    kafkaProducerService.sendMessage("order-topic", orderDto);

	    return orderMapper.toDto(savedOrder);
	}


	
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> {
                    OrderDto dto = new OrderDto();
                    dto.setId(order.getId());
                    dto.setCustomerId(order.getCustomerId());
                    dto.setOrderDate(order.getOrderDate());
                    dto.setStatus(order.getStatus());
                    dto.setTotalAmount(order.getTotalAmount());

                    List<OrderItemObj> items = order.getItems().stream()
                            .map(item -> {
                                OrderItemObj obj = new OrderItemObj();
                                obj.setId(item.getId());
                                obj.setProductId(item.getProductId());
                                obj.setQuantity(item.getQuantity());
                                obj.setPrice(item.getPrice());
                                obj.setSubtotal(item.getSubtotal());
                                return obj;
                            })
                            .toList();

                    dto.setItems(items);
                    return dto;
                })
                .toList();
    }

	
	public Optional<OrderDto> getOrderById(Long id){
		return orderRepository.findById(id)
				.map(order -> {
                    OrderDto dto = new OrderDto();
                    dto.setId(order.getId());
                    dto.setCustomerId(order.getCustomerId());
                    dto.setOrderDate(order.getOrderDate());
                    dto.setStatus(order.getStatus());
                    dto.setTotalAmount(order.getTotalAmount());

                    List<OrderItemObj> items = order.getItems().stream()
                            .map(item -> {
                                OrderItemObj obj = new OrderItemObj();
                                obj.setId(item.getId());
                                obj.setProductId(item.getProductId());
                                obj.setQuantity(item.getQuantity());
                                obj.setPrice(item.getPrice());
                                obj.setSubtotal(item.getSubtotal());
                                return obj;
                            })
                            .toList();

                    dto.setItems(items);
                    return dto;
				});
	}
	
	public void deleteOrder(Long id) {
		orderRepository.deleteById(id);
	}
	
	public OrderDto updateOrderStatus(String status, Long id) {
	    return orderRepository.findById(id)
	            .map(order -> {
	                // 1. Update status
	                order.setStatus(status);
	                Order updatedOrder = orderRepository.save(order);

	                // 2. Convert to DTO
	                OrderDto dto = new OrderDto();
	                dto.setId(updatedOrder.getId());
	                dto.setCustomerId(updatedOrder.getCustomerId());
	                dto.setOrderDate(updatedOrder.getOrderDate());
	                dto.setStatus(updatedOrder.getStatus());
	                dto.setTotalAmount(updatedOrder.getTotalAmount());

	                List<OrderItemObj> items = updatedOrder.getItems().stream()
	                        .map(item -> {
	                            OrderItemObj obj = new OrderItemObj();
	                            obj.setId(item.getId());
	                            obj.setProductId(item.getProductId());
	                            obj.setQuantity(item.getQuantity());
	                            obj.setPrice(item.getPrice());
	                            obj.setSubtotal(item.getSubtotal());
	                            return obj;
	                        })
	                        .toList();

	                dto.setItems(items);
	                return dto;
	            })
	            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
	}

}
