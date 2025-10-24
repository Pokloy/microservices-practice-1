package com.example.demo.model.obj;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemObj {
    private Long id;

    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}