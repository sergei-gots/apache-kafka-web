package org.appsdeveloperblog.ws.emailnotification.handler;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.appsdeveloperblog.ws.core.events.ProductCreatedEvent;
import org.appsdeveloperblog.ws.emailnotification.io.ProcessedEventEntity;
import org.appsdeveloperblog.ws.emailnotification.io.ProcessedEventRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}")
class ProductCreatedEventHandlerIT {

    @MockitoBean
    ProcessedEventRepository processedEventRepository;

    @MockitoBean
    RestTemplate restTemplate;

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

        ProcessedEventEntity processedEventEntity = new ProcessedEventEntity();

        when(processedEventRepository.findByMessageId(anyString()))
                .thenReturn(Optional.of(processedEventEntity));
        when(processedEventRepository.save(any(ProcessedEventEntity.class)))
                .thenReturn(null);

        HttpHeaders httpHeaders = new HttpHeaders();
        String responseBody = "{\"key\":\"value\"}";
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<@NotNull String> responseEntity = new ResponseEntity<>(responseBody, httpHeaders, HttpStatus.OK);

        when(restTemplate.exchange(
                any(String.class),
                any(HttpMethod.class),
                isNull(),
                eq(String.class))
        ).thenReturn(responseEntity);

        // Act

        // Assert



    }
}