package org.appsdeveloperblog.ws.emailnotification.handler;

import lombok.extern.slf4j.Slf4j;
import org.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductCreatedEventHandler {

    @KafkaListener(topics = "product-created-events-topic")
    public void handle(ProductCreatedEvent productCreatedEvent) {
        log.info("Received a new event: {}", productCreatedEvent.getTitle());
    }
}
