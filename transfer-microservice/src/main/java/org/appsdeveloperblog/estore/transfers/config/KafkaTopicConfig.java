package org.appsdeveloperblog.estore.transfers.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.appsdeveloperblog.ws.core.config.KafkaTopicFactory;
import org.appsdeveloperblog.ws.core.config.KafkaTopicsProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@EnableConfigurationProperties(KafkaTopicsProperties.class)
public class KafkaTopicConfig {

    @Value("${withdrawal-events-topic}")
    private String withdrawalEventsTopic;

    @Value("${deposit-events-topic}")
    private String depositEventsTopic;

    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties properties) {
        return new KafkaAdmin(properties.buildAdminProperties());
    }

    @Bean
    public KafkaTopicFactory kafkaTopicFactory(KafkaTopicsProperties kafkaTopicsProperties) {
        return new KafkaTopicFactory(kafkaTopicsProperties);
    }

    @Bean
    public NewTopic withdrawTopic(KafkaTopicFactory kafkaTopicFactory) {
        return kafkaTopicFactory.createTopic(withdrawalEventsTopic);
    }

    @Bean
    public NewTopic depositTopic(KafkaTopicFactory kafkaTopicFactory) {
        return kafkaTopicFactory.createTopic(depositEventsTopic);
    }

}
