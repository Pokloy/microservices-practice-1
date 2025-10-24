package com.example.demo.model.openFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.controller.dto.CustomerWebDto;

@FeignClient(name = "customer-service")
public interface CustomerClient {
    @GetMapping("/customers/{id}")
    CustomerWebDto getCustomerById(@PathVariable("id") Long id);
}
