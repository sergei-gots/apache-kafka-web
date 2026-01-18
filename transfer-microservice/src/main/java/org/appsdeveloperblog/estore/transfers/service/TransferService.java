package org.appsdeveloperblog.estore.transfers.service;

import org.appsdeveloperblog.ws.core.model.TransferRestModel;
import org.springframework.transaction.annotation.Transactional;

public interface TransferService {
    boolean transferUsingTransactionalExample(TransferRestModel transferRestModel);

    @Transactional(transactionManager = "kafkaTransactionManager")
    boolean transferUsingExecuteInTransactionExample(TransferRestModel transferRestModel);
}
