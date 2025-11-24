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
    String retries;

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

    Map<String, Object> producerProperties() {

        Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        properties.put(ProducerConfig.ACKS_CONFIG, acknowledgmentLevel);
        properties.put(ProducerConfig.RETRIES_CONFIG, retries);
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, retryBackOff);
        properties.put(ProducerConfig.RETRY_BACKOFF_MAX_MS_CONFIG, retryBackOffMax);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, requestTimeout);
        properties.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeout);

        return properties;
    }

    @Bean
    ProducerFactory<String, ProductCreatedEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProperties());
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
