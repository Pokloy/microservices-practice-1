package com.example.demo.model.mapper;

import org.mapstruct.Mapper;

import com.example.demo.model.dao.entity.Order;
import com.example.demo.model.dto.OrderDto;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order entity);
    Order toEntity(OrderDto dto);
}

