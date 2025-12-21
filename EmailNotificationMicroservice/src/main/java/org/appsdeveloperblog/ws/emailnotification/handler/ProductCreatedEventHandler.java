package org.appsdeveloperblog.ws.emailnotification.handler;

import lombok.extern.slf4j.Slf4j;
import org.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.appsdeveloperblog.ws.emailnotification.error.NonRetryableException;
import org.appsdeveloperblog.ws.emailnotification.error.RetryeableException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class ProductCreatedEventHandler {

    private final RestTemplate restTemplate;

    public  ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "product-created-events-topic")
    public void handle(ProductCreatedEvent productCreatedEvent) {

        log.info("Received a new event: {}", productCreatedEvent.getTitle());

        checkProductQuanity(productCreatedEvent);

        sendHttpRequest();
    }

    private void checkProductQuanity(ProductCreatedEvent productCreatedEvent) {

        if (productCreatedEvent.getQuantity() < 0) {
            log.warn("Product quantity {} is less than 0. A non-retryable exception will be thrown.", productCreatedEvent.getQuantity());
            throw new NonRetryableException("Product quantity is less than 0.");
        }
    }

    private void sendHttpRequest() {

        String url = "http://localhost:8082";

        try {
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        }
        catch (ResourceAccessException e) {
            log.warn("Cannot access resource {}", url);
            throw new RetryeableException(e);
        }
        catch (HttpServerErrorException e ) {
            log.warn("Caught http server exception", e);
            throw new NonRetryableException(e);
        }
        catch (Exception e ) {
            log.warn("Caught an exception", e);
            throw new NonRetryableException(e);
        }
    }
}
