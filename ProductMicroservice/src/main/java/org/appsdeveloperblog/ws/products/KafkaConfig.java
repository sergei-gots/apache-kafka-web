package org.appsdeveloperblog.ws.products;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.appsdeveloperblog.ws.products.service.ProductCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    String bootStrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    String valueSerializer;

    @Value("${spring.kafka.producer.acks}")
    String acknowledgmentLevel;

    @Value("${spring.kafka.producer.retries}")
    Integer retries;

    @Value("${spring.kafka.producer.properties.retry.backoff.ms}")
    String retryBackOff;

    @Value("${spring.kafka.producer.properties.retry.backoff.max.ms}")
    String retryBackOffMax;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    String deliveryTimeout;

    @Value("${spring.kafka.producer.properties.linger.ms}")
    String linger;

    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    String requestTimeout;

    @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    Integer maxInFlightRequests;

    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    Boolean idempotance;

    Map<String, Object> producerConfigs() {

        Map<String, Object> configs = new HashMap<>();

        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);

        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        configs.put(ProducerConfig.ACKS_CONFIG, acknowledgmentLevel);
        configs.put(ProducerConfig.RETRIES_CONFIG, retries);
        configs.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, retryBackOff);
        configs.put(ProducerConfig.RETRY_BACKOFF_MAX_MS_CONFIG, retryBackOffMax);
        configs.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        configs.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        configs.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequests);
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotance);

        return configs;
    }

    @Bean
    ProducerFactory<String, ProductCreatedEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    NewTopic createTopic() {
        return TopicBuilder.name("product-created-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(Map.of("min.insync.replicas", "2"))
                .build();
    }
}
