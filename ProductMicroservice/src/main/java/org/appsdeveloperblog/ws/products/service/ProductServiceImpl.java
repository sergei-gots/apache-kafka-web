package org.appsdeveloperblog.ws.products.service;

import lombok.extern.slf4j.Slf4j;
import org.appsdeveloperblog.ws.products.rest.CreateProductRestModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
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

        SendResult<String, ProductCreatedEvent> result =
            kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get();

        log.info("- Topic: {}", result.getRecordMetadata().topic());
        log.info("- Partition: {}", result.getRecordMetadata().partition());
        log.info("- Offset: {}", result.getRecordMetadata().offset());
        log.info("- Timestamp: {}", Instant.ofEpochMilli(result.getRecordMetadata().timestamp()));

        log.info("*** Returning product id: {}", productId);

        return productId;
    }
}
