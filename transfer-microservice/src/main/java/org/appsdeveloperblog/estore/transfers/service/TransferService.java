package org.appsdeveloperblog.estore.transfers.service;

import org.appsdeveloperblog.estore.transfers.model.TransferRestModel;

public interface TransferService {
    boolean transfer(TransferRestModel transferRestModel);
}
