package com.example.demo.controller.dto;

import lombok.Data;

@Data
public class CustomerWebDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
}
