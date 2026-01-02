package org.appsdeveloperblog.ws.products;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.appsdeveloperblog.ws.core.AppKafkaConfig;
import org.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Import(AppKafkaConfig.class)
public class KafkaProducerConfig {

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
    Boolean idempotence;

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
        configs.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);

        return configs;
    }

    @Bean
    ProducerFactory<@NotNull String, @NotNull ProductCreatedEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<@NotNull String, @NotNull ProductCreatedEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
