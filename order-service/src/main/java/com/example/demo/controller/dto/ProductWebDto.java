package com.example.demo.controller.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductWebDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private int stock;
}
