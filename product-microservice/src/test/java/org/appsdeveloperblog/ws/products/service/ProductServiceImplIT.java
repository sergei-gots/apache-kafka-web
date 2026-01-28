package org.appsdeveloperblog.ws.products.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.appsdeveloperblog.ws.core.events.ProductCreatedEvent;
import org.appsdeveloperblog.ws.products.rest.CreateProductRestModel;
import org.awaitility.Awaitility;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *  To run this integration test separately ('-Dit.test') use 'failsafe' ('verify') plugin:
 *  This test as IT (integration test) must be run with 'failsafe' plugin (stage 'verify').
 *  Use the next command using Maven Wrapper (./mvnw)
 *  ('-X' specifies DEBUG MODE output):
 *      <code>./mvnw clean verify -Dit.test=org.appsdeveloperblog.ws.products.service.ProductServiceImplIT -X</code>
 */
//Consider TestContainers as an alternative to @EmbeddedKafka
//Consider using partitions = 1 with no other options
@EmbeddedKafka(partitions = 1, // partitions = 3, count = 3,
        controlledShutdown = true,
        brokerProperties = {
                "process.roles=broker,controller",  // combined
                "node.id=1",
                "controller.quorum.voters=1@localhost:0"  // 0 = random port
        }
)
@ActiveProfiles("test")
@SpringBootTest(properties = "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}")
@DirtiesContext
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceImplIT {

    @Autowired
    ProductService productService;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    Environment environment;

    private KafkaMessageListenerContainer<@NotNull String, @NotNull ProductCreatedEvent> messageListenerContainer;
    private BlockingQueue<ConsumerRecord<String, ProductCreatedEvent>> records;

    @BeforeAll
    public void setUp() {
        DefaultKafkaConsumerFactory<@NotNull String, @NotNull Object>
                consumerFactory = new DefaultKafkaConsumerFactory<>(getConsumerProperties());

        ContainerProperties containerProperties = new ContainerProperties(
                Objects.requireNonNull(environment.getProperty("product-created-events-topic"))
        );

        messageListenerContainer = new KafkaMessageListenerContainer<>(
                consumerFactory, containerProperties
        );

        records = new LinkedBlockingQueue<>();

        messageListenerContainer.setupMessageListener(
                //Here is an implementation of @FunctionalInterface MessageListener<T> :
                (MessageListener<@NotNull String, @NotNull ProductCreatedEvent>)
                //This is the body of the method GenericMessageListener<T>.onMessage(T message):
                        //"WHEN the message is arrived THEN add it to the records' queue"
                        records::add
        );

        messageListenerContainer.start();
    }

    @AfterAll
    void tearDown() {
        messageListenerContainer.stop();
    }


    @Test
    void givenValidData_whenCreateProduct_thenKafkaMessageIsSent() throws Exception {

        //Arrange
        String title = "iPhone 16";
        BigDecimal price = new BigDecimal(600);
        Integer quantity = 1;

        CreateProductRestModel createProductRestModel = new CreateProductRestModel();
        createProductRestModel.setTitle(title);
        createProductRestModel.setPrice(price);
        createProductRestModel.setQuantity(quantity);

        //Act
        productService.createProduct(createProductRestModel);

        //Assert
        ConsumerRecord<String, ProductCreatedEvent> message =
                Awaitility.await()
                        .atMost(101, TimeUnit.MILLISECONDS)
                        .until(() -> this.records.poll(), Objects::nonNull);

        ProductCreatedEvent productCreatedEvent = message.value();
        assertThat(productCreatedEvent.getTitle()).isEqualTo(title);
        assertThat(productCreatedEvent.getPrice()).isEqualTo(price);
        assertThat(productCreatedEvent.getQuantity()).isEqualTo(quantity);

    }

    private Map<String, Object> getConsumerProperties() {

        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class,
                JacksonJsonDeserializer.TRUSTED_PACKAGES, Objects.requireNonNull(environment.getProperty("spring.kafka.consumer.properties.spring.json.trusted.packages")),
                ConsumerConfig.GROUP_ID_CONFIG, Objects.requireNonNull(environment.getProperty("spring.kafka.consumer.group-id")),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, Objects.requireNonNull(environment.getProperty("spring.kafka.consumer.auto-offset-reset"))
        );
    }

}