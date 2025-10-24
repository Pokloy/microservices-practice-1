package com.example.demo.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.obj.OrderItemObj;

import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private Long customerId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    
    List<OrderItemObj> items = new ArrayList<>();
}
