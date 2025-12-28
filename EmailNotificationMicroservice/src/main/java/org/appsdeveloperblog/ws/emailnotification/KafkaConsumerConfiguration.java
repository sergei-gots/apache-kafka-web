package org.appsdeveloperblog.ws.emailnotification;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.appsdeveloperblog.ws.emailnotification.error.NonRetryableException;
import org.appsdeveloperblog.ws.emailnotification.error.RetryableException;

@Configuration
public class KafkaConsumerConfiguration {

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
    ProducerFactory<@NotNull String, @NotNull Object> producerFactory(KafkaProperties kafkaProperties) {

        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildProducerProperties());
    }

}
