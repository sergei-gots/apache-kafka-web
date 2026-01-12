package org.appsdeveloperblog.estore.transfers.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import org.appsdeveloperblog.estore.transfers.model.TransferRestModel;
import org.appsdeveloperblog.estore.transfers.service.TransferService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/transfers")
@Slf4j
public class TransfersController {

    private final TransferService transferService;

    public TransfersController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping()
    public boolean transfer(@RequestBody TransferRestModel transferRestModel) {
        return transferService.transfer(transferRestModel);
    }
}
