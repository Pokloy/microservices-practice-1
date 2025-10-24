package com.example.demo.model.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import com.example.demo.model.dto.OrderDto;

@Service
public class OrderEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);

//    @Retryable(
//            value = { Exception.class },          // Retry for all exceptions
//            maxAttempts = 3,                      // Try up to 3 times
//            backoff = @Backoff(delay = 2000)      // Wait 2 seconds before retrying
//        )
    @KafkaListener( topics = "order-topic", 
    				groupId = "inventory-group", 
    				containerFactory = "kafkaListenerContainerFactory")
    public void consumeOrder(OrderDto orderDto) {
        logger.info("Received Order Event: {}", orderDto);
        
        try {
            // Simulate processing logic
            if (orderDto.getId() == null) {
                throw new IllegalArgumentException("Invalid order data!");
            }
        	
            // 🧠 Simulate inventory update logic (temporary failure for demo)
            if (Math.random() < 0.3) {  // 30% chance to fail
                throw new RuntimeException("Simulated transient failure updating inventory");
            }

            logger.info("✅ Inventory updated successfully for Order ID: {}", orderDto.getId());

        } catch (Exception e) {
            logger.error("❌ Error processing order event: {}", e.getMessage());
            throw e; // Rethrow to trigger retry
        }
    }
    
    // Optional: Dead Letter Topic listener
    @KafkaListener(topics = "order-topic.DLT", groupId = "inventory-group")
    public void consumeDLT(String failedMessage) {
        logger.warn("⚠️ Message moved to DLT: {}", failedMessage);
    }
}
