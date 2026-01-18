package org.appsdeveloperblog.estore.transfers.service;

import org.appsdeveloperblog.ws.core.model.TransferRestModel;

public interface TransferService {

    boolean transfer(TransferRestModel transferRestModel);

}
