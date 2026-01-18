package com.appsdeveloperblog.estore.WithdrawalService.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import org.appsdeveloperblog.ws.core.events.WithdrawalRequestedEvent;

@Component
@Slf4j
@KafkaListener(topics = "${withdrawal-events-topic}", containerFactory = "kafkaListenerContainerFactory")
public class WithdrawalRequestedEventHandler {

    @KafkaHandler
    public void handle(@Payload WithdrawalRequestedEvent withdrawalRequestedEvent) {
        log.info("Received a new withdrawal event. amount: {} ", withdrawalRequestedEvent.getAmount());
    }
}
