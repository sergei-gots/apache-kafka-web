package org.appsdeveloperblog.ws.core.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.appsdeveloperblog.ws.core.model.TransferRestModel;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequestedEvent {

    private String senderId;
    private String recipientId;
    private BigDecimal amount;

    public static WithdrawalRequestedEvent of(TransferRestModel transferRestModel) {
        return new WithdrawalRequestedEvent(
            transferRestModel.getSenderId(),
            transferRestModel.getRecipientId(),
            transferRestModel.getAmount()
        );
    }

}