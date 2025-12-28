package org.appsdeveloperblog.ws.emailnotification;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.appsdeveloperblog.ws.emailnotification.error.NonRetryableException;
import org.appsdeveloperblog.ws.emailnotification.error.RetryableException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {

    @Autowired
    Environment environment;

    @Bean
    ConsumerFactory<@NotNull String, @NotNull Object> consumerFactory(KafkaProperties kafkaProperties) {

        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties()); //kafkaConsumerConfigs());
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<@NotNull String, @NotNull Object> kafkaListenerContainerFactory(
            ConsumerFactory<@NotNull String, @NotNull Object> consumerFactory,
            KafkaTemplate<@NotNull String, @NotNull Object> kafkaTemplate
    ) {

        ConcurrentKafkaListenerContainerFactory<@NotNull String, @NotNull Object> listenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();

        DeadLetterPublishingRecoverer deadLetterPublishingRecoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                deadLetterPublishingRecoverer,
                new FixedBackOff(5_000L, 3L));

        errorHandler.addNotRetryableExceptions(NonRetryableException.class);
        errorHandler.addRetryableExceptions(RetryableException.class);

        listenerContainerFactory.setConsumerFactory(consumerFactory);
        listenerContainerFactory.setCommonErrorHandler(errorHandler);

        return listenerContainerFactory;
    }

    @Bean
    KafkaTemplate<@NotNull String, @NotNull Object> kafkaTemplate(
            ProducerFactory<@NotNull String, @NotNull Object> producerFactory
    ) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    ProducerFactory<@NotNull String, @NotNull Object> producerFactory() {

        Map<String, Object> producerConfigs = new HashMap<>();

        producerConfigs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        producerConfigs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,  StringSerializer.class);
        producerConfigs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  JacksonJsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(producerConfigs);
    }

}
