package org.appsdeveloperblog.ws.emailnotification.handler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}")
class ProductCreatedEventHandlerIT {

    @Test
    public void whenNothing_thenOk() {

    }
}