package com.example.demo.common.config;

import org.mapstruct.Mapper;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.model.dao.entity.Order;
import com.example.demo.model.dto.OrderDto;



@Configuration
public class AppConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    
    @Mapper(componentModel = "spring")
    public interface OrderMapper {
        OrderDto toDto(Order entity);
        Order toEntity(OrderDto dto);
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
