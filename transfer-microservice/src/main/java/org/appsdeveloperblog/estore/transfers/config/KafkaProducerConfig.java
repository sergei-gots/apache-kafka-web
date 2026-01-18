package org.appsdeveloperblog.estore.transfers.config;

import jakarta.persistence.EntityManagerFactory;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;

@Configuration
@EnableTransactionManagement
@Import(KafkaTopicConfig.class)
public class KafkaProducerConfig {

    @Value("${spring.kafka.producer.transactional-id-prefix}")
    String transactionalIdPrefix;

    @Bean
    public ProducerFactory<String, Object> producerFactory(KafkaProperties kafkaProperties) {

        Map<String, Object> props = kafkaProperties.buildProducerProperties();
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalIdPrefix);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean("kafkaTransactionManager")
    public KafkaTransactionManager<@NotNull String, @NotNull Object> kafkaTransactionManager(
            ProducerFactory<@NotNull String, @NotNull Object> pf
    ) {
        return new KafkaTransactionManager<>(pf);
    }

    @Bean("transactionManager")
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

}
