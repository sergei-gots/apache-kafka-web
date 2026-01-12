package org.appsdeveloperblog.ws.products.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.appsdeveloperblog.ws.core.config.KafkaTopicFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.appsdeveloperblog.ws.core.config.KafkaTopicsProperties;

@Configuration
@EnableConfigurationProperties(KafkaTopicsProperties.class)
public class KafkaTopicConfig {

    @Value("${app.kafka.topics.product-created-events-topic}")
    private String productCreatedEventsTopicName;

    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties properties) {
        return new KafkaAdmin(properties.buildAdminProperties());
    }

    @Bean
    public KafkaTopicFactory kafkaTopicFactory(KafkaTopicsProperties kafkaTopicsProperties) {
        return new KafkaTopicFactory(kafkaTopicsProperties);
    }

    @Bean
    public NewTopic productCreatedEventTopic(KafkaTopicFactory kafkaTopicFactory) {
        return kafkaTopicFactory.createTopic(productCreatedEventsTopicName);
    }

}
