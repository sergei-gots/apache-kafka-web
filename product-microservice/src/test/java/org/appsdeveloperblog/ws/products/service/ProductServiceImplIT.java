package org.appsdeveloperblog.ws.products.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

//Consider TestContainers as an alternative to @EmbeddedKafka
//Consider using partitions = 1 with no other options
@EmbeddedKafka(partitions = 3, count = 3, controlledShutdown = true)
@ActiveProfiles("test")
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceImplIT {

    @Test
    void givenValidData_whenCreateProduct_thenKafkaMessageIsSent() {

        //Arrange

        //Act

        //Assert

    }

}