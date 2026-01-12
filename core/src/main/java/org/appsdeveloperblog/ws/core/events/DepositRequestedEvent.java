package org.appsdeveloperblog.ws.core.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequestedEvent {
    
    private String senderId;
    private String recipientId;
    private BigDecimal amount;
 }