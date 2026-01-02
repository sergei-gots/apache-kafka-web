package org.appsdeveloperblog.ws.core;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@EnableConfigurationProperties(AppKafkaTopicsProperties.class)
public class AppKafkaConfig {

    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties properties) {
        return new KafkaAdmin(properties.buildAdminProperties());
    }

    @Bean
    public NewTopic productCreatedEventTopic(AppKafkaTopicsProperties p) {
        return TopicBuilder.name(p.getProductCreatedEvents())
                .partitions(p.getPartitions())
                .replicas(p.getReplicas())
                .configs(p.getConfigs())
                .build();
    }

    @Bean
    public NewTopic productCreatedEventsDlt(AppKafkaTopicsProperties topics) {
        return TopicBuilder.name(topics.getProductCreatedEvents() + "-dlt")
                .partitions(topics.getPartitions())
                .replicas(topics.getReplicas())
                .build();
    }

}
