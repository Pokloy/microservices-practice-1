package com.example.demo.model.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.kafka.annotation.KafkaListener;

public class OrderEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    @KafkaListener(topics = "order-events", groupId = "product-group")
    public void consumeOrderEvent(OrderDto order) {
        log.info("Received order event: {}", order);
        // Here you can update product stock, analytics, etc.
    }
}
