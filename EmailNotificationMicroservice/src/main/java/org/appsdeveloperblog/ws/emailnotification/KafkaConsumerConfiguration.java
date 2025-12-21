package org.appsdeveloperblog.ws.emailnotification;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.appsdeveloperblog.ws.emailnotification.error.NonRetryableException;
import org.appsdeveloperblog.ws.emailnotification.error.RetryableException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {

    @Autowired
    Environment environment;

    Map<String, Object> kafkaConsumerConfigs() {

        Map<String, Object> consumerConfigs = new HashMap<>();

        consumerConfigs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.consumer.bootstrap-servers"));
        consumerConfigs.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        consumerConfigs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfigs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        consumerConfigs.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);
        consumerConfigs.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, ProductCreatedEvent.class);
        consumerConfigs.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages"));
        consumerConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.consumer.group-id"));

        return consumerConfigs;
    }

    @Bean
    ConsumerFactory<@NotNull String, @NotNull Object> consumerFactory() {

        return new DefaultKafkaConsumerFactory<>(kafkaConsumerConfigs());
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
