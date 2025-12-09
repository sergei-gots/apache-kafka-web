package org.appsdeveloperblog.ws.emailnotification.handler;

import lombok.extern.slf4j.Slf4j;
import org.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.appsdeveloperblog.ws.emailnotification.error.NonRetryableException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductCreatedEventHandler {

    @KafkaListener(topics = "product-created-events-topic")
    public void handle(ProductCreatedEvent productCreatedEvent) {
        log.info("Received a new event: {}", productCreatedEvent.getTitle());
        if (productCreatedEvent.getQuantity() < 0) {
            log.warn("Product quantity {} is less than 0. A non-retryable exception will be thrown.", productCreatedEvent.getQuantity());
            throw new NonRetryableException("Product quantity is less than 0.");
        }
    }
}
