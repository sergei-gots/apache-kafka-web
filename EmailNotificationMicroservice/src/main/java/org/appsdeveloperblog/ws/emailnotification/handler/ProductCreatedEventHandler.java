package org.appsdeveloperblog.ws.emailnotification.handler;

import lombok.extern.slf4j.Slf4j;
import org.appsdeveloperblog.ws.core.ProductCreatedEvent;
import org.appsdeveloperblog.ws.emailnotification.error.NonRetryableException;
import org.appsdeveloperblog.ws.emailnotification.error.RetryableException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

        checkProductQuantity(productCreatedEvent);
        sendHttpRequest();

        log.info("The event {} has been successfully handled", productCreatedEvent.getTitle());
    }

    private void checkProductQuantity(ProductCreatedEvent productCreatedEvent) {

        if (productCreatedEvent.getQuantity() < 0) {
            log.warn("Product quantity {} is less than 0. A non-retryable exception will be thrown.", productCreatedEvent.getQuantity());
            throw new NonRetryableException("Product quantity is less than 0.");
        }
    }

    private int sendCounter = 0;

    private void sendHttpRequest() {

        String url = "http://localhost:8082/response/" + ((sendCounter++%2 == 0) ? "500" : "200");

        try {
            ResponseEntity<@NotNull String> response =
                    restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Received response with an http status 'OK' from a remote service. Response body:{}",
                        response.getBody()
                );
            }
        }
        catch (ResourceAccessException e) {
            log.warn("Cannot access resource {}", url, e);
            throw new RetryableException(e);
        }
        catch (HttpServerErrorException e ) {
            HttpStatusCode httpStatusCode = e.getStatusCode();
            if (httpStatusCode.is5xxServerError()) {
                log.warn("Caught 5xx http server exception {}", httpStatusCode, e);
                throw new RetryableException(e);
            } else  {
                log.warn("Caught not-a-5xx http server exception", e);
                throw new NonRetryableException(e);
            }
        }
        catch (Exception e ) {
            log.warn("Caught an exception", e);
            throw new NonRetryableException(e);
        }
    }
}
