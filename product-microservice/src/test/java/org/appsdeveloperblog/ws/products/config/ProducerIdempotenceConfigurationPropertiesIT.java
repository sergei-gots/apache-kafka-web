package org.appsdeveloperblog.ws.products.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.appsdeveloperblog.ws.core.events.ProductCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *  To run this integration test separately ('-Dit.test') use 'failsafe' ('verify') plugin:
 *  This test as IT (integration test) must be run with 'failsafe' plugin (stage 'verify').
 *  Use the next command using Maven Wrapper (./mvnw)
 *  ('-X' specifies DEBUG MODE output):
 *     <code>./mvnw clean verify -Dit.test=org.appsdeveloperblog.ws.products.config.ProducerIdempotenceIT -X</code>
 */
@SpringBootTest
public class ProducerIdempotenceConfigurationPropertiesIT {

    @MockitoBean
    KafkaAdmin kafkaAdmin;

    @Autowired
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    @Test
    void testProducerConfigProperties() {

        //Arrange
        ProducerFactory<String, ProductCreatedEvent> producerFactory =
                kafkaTemplate.getProducerFactory();
        Map<String, Object> producerConfigProps = producerFactory.getConfigurationProperties();

        //Assert
        assertThat(producerConfigProps.get(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG))
                .isEqualTo("true");

        assertThat(producerConfigProps.get(ProducerConfig.ACKS_CONFIG))
                .isEqualTo("all");

        if (producerConfigProps.containsKey(ProducerConfig.RETRIES_CONFIG)) {
            assertThat(
                Integer.parseInt(
                        producerConfigProps.get(ProducerConfig.RETRIES_CONFIG)
                            .toString()
                )
            ).isGreaterThan(0);

        }

        if (producerConfigProps.containsKey(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION)) {
            assertThat(
                    Integer.parseInt(producerConfigProps.get(
                            ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION)
                    .toString()
                    )
            ).isBetween(1, 5);
        }

    }
}
