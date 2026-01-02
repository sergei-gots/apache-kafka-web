package org.appsdeveloperblog.ws.products;

import org.appsdeveloperblog.ws.core.AppKafkaConfig;
import org.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@Import(AppKafkaConfig.class)
public class KafkaProducerConfig {

    @Bean
    ProducerFactory<@NotNull String, @NotNull ProductCreatedEvent> producerFactory(
            KafkaProperties kafkaProperties
    ) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    }

    @Bean
    KafkaTemplate<@NotNull String, @NotNull ProductCreatedEvent> kafkaTemplate(
            KafkaProperties kafkaProperties
    ) {
        return new KafkaTemplate<>(producerFactory(kafkaProperties));
    }

}
