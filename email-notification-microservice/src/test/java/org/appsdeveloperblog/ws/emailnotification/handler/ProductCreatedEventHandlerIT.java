package org.appsdeveloperblog.ws.emailnotification.handler;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.appsdeveloperblog.ws.core.events.ProductCreatedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.math.BigDecimal;
import java.util.UUID;

@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}")
class ProductCreatedEventHandlerIT {

    @DisplayName("handle should succeed")
    @Test
    public void shouldSucceed() {

        // Arrange
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        productCreatedEvent.setTitle("Test Product");
        productCreatedEvent.setProductId(UUID.randomUUID().toString());
        productCreatedEvent.setPrice(BigDecimal.TEN);
        productCreatedEvent.setQuantity(1);

        String messageId = UUID.randomUUID().toString();
        String messageKey = productCreatedEvent.getProductId();

        ProducerRecord<String, ProductCreatedEvent> record = new ProducerRecord<>(
                "product-created-events-topic",
                messageKey,
                productCreatedEvent
        );

        record.headers().add("messageId", messageId.getBytes());
        record.headers().add(KafkaHeaders.RECEIVED_KEY, messageKey.getBytes());


        // Act


        // Assert



    }
}