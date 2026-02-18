package org.appsdeveloperblog.ws.emailnotification.handler;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.appsdeveloperblog.ws.core.events.ProductCreatedEvent;
import org.appsdeveloperblog.ws.emailnotification.io.ProcessedEventEntity;
import org.appsdeveloperblog.ws.emailnotification.io.ProcessedEventRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}")
class ProductCreatedEventHandlerIT {

    @MockitoBean
    ProcessedEventRepository processedEventRepository;

    @MockitoBean
    RestTemplate restTemplate;

    @Autowired
    KafkaTemplate<@NotNull String, @NotNull Object> kafkaTemplate;

    @MockitoSpyBean
    ProductCreatedEventHandler productCreatedEventHandler;

    /**
     * Make sure that <code>spring.kafka.consumer.auto-offset-reset: earliest</code> is set
     * for the test configuration
     */
    @DisplayName("Kafka consumer should call handle once with correct ProductCreatedEvent when published")
    @Test
    public void shouldCallHandleOnceWithCorrectArguments_whenProductCreatedEventIsPublished() throws Exception {

        // Arrange
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        productCreatedEvent.setTitle("Test Product");
        productCreatedEvent.setProductId(UUID.randomUUID().toString());
        productCreatedEvent.setPrice(BigDecimal.TEN);
        productCreatedEvent.setQuantity(1);

        String messageId = UUID.randomUUID().toString();
        String messageKey = productCreatedEvent.getProductId();

        ProducerRecord<String, Object> record = new ProducerRecord<>(
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

        mockSendingHttpRequest();

        // Act
        kafkaTemplate.send(record).get();

        // Assert
        ArgumentCaptor<String> messageIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ProductCreatedEvent> productCreatedEventCaptor = ArgumentCaptor.forClass(ProductCreatedEvent.class);

        verify(productCreatedEventHandler, timeout(5000).times(1))
                .handle(
                        productCreatedEventCaptor.capture(),
                        messageIdCaptor.capture(),
                        messageKeyCaptor.capture()
                        );

        assertThat(messageIdCaptor.getValue())
                .isEqualTo(messageId);

        assertThat(messageKeyCaptor.getValue())
                .isEqualTo(messageKey);

        assertThat(productCreatedEventCaptor.getValue())
                .usingRecursiveComparison()
                .isEqualTo(productCreatedEvent);
    }

    private void mockSendingHttpRequest() {
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
    }
}