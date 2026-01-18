package org.appsdeveloperblog.estore.transfers.service;

import lombok.extern.slf4j.Slf4j;
import org.appsdeveloperblog.ws.core.error.RetryableException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import org.appsdeveloperblog.estore.transfers.error.TransferServiceException;
import org.appsdeveloperblog.ws.core.model.TransferRestModel;
import org.appsdeveloperblog.ws.core.events.DepositRequestedEvent;
import org.appsdeveloperblog.ws.core.events.WithdrawalRequestedEvent;

@Slf4j
@Service
public class TransferServiceImpl implements TransferService {

    @Value("${withdrawal-events-topic}")
    private String withdrawalEventsTopic;

    @Value("${deposit-events-topic}")
    private String depositEventsTopic;

    private final KafkaTemplate<@NotNull String, @NotNull Object> kafkaTemplate;
    private final RestTemplate restTemplate;

    public TransferServiceImpl(KafkaTemplate<@NotNull String, @NotNull Object> kafkaTemplate,
                               RestTemplate restTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional(transactionManager = "kafkaTransactionManager")
    public boolean transferUsingTransactionalExample(TransferRestModel transferRestModel) {

        WithdrawalRequestedEvent withdrawalEvent = WithdrawalRequestedEvent.of(transferRestModel);
        DepositRequestedEvent depositEvent = DepositRequestedEvent.of(transferRestModel);

        try {
            doTransfer(withdrawalEvent, depositEvent);

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new TransferServiceException(ex);
        }

        return true;
    }

    @Override
    public boolean transferUsingExecuteInTransactionExample(TransferRestModel transferRestModel) {

        WithdrawalRequestedEvent withdrawalEvent = WithdrawalRequestedEvent.of(transferRestModel);
        DepositRequestedEvent depositEvent = DepositRequestedEvent.of(transferRestModel);

        return Boolean.TRUE.equals(kafkaTemplate.executeInTransaction(t -> {

            doTransfer(withdrawalEvent, depositEvent);

            return true;
        }));
    }

    /**
     * Inside this method there is a business logic that is able to cause a RetryableException
     *
     */
    private void doTransfer(WithdrawalRequestedEvent withdrawalEvent, DepositRequestedEvent depositEvent) {
        kafkaTemplate.send(withdrawalEventsTopic, withdrawalEvent);
        log.info("Sent event to {}.", withdrawalEventsTopic);

        kafkaTemplate.send(depositEventsTopic, depositEvent);
        log.info("Sent event to {}.", depositEventsTopic);

        //The business logic that is able to cause an error
        callRemoteService();
    }

    /**
     * @throws RetryableException if the destination Http-Service is not available
     */
    private void callRemoteService() {

        String requestUrl = "http://localhost:8082/response/200";
        ResponseEntity<@NotNull String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);

        if (response.getStatusCode().value() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            throw new RetryableException("Destination Http-Service is not available");
        }

        if (response.getStatusCode().value() == HttpStatus.OK.value()) {
            log.info("Received response from mock service: {}", response.getBody());
        }

    }

}
