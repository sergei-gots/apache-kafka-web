package org.appsdeveloperblog.ws.products.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.appsdeveloperblog.ws.products.rest.CreateProductRestModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Value("${kafka.topic}")
    String kafkaTopic;

    private final KafkaTemplate<@NotNull String, @NotNull ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<@NotNull String, @NotNull ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel productRestModel) throws Exception {

        String productId = UUID.randomUUID().toString();

        // TODO: Persist Product Details into database table before publishing an Event

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,
                productRestModel.getTitle(),
                productRestModel.getPrice(),
                productRestModel.getQuantity()
        );

        log.info("*** Before publishing ProductCreatedEvent");

        ProducerRecord<@NotNull String, @Nullable ProductCreatedEvent> record = new ProducerRecord<>(
            kafkaTopic, productId, productCreatedEvent
        );
       // record.headers().add("messageId", UUID.randomUUID().toString().getBytes());
        record.headers().add("messageId", "123".getBytes());

        SendResult<@NotNull String, @NotNull ProductCreatedEvent> result =
            kafkaTemplate.send(record).get();

        log.info("- Topic: {}", result.getRecordMetadata().topic());
        log.info("- Partition: {}", result.getRecordMetadata().partition());
        log.info("- Offset: {}", result.getRecordMetadata().offset());
        log.info("- Timestamp: {}", Instant.ofEpochMilli(result.getRecordMetadata().timestamp()));

        log.info("*** Returning product id: {}", productId);

        return productId;
    }
}
