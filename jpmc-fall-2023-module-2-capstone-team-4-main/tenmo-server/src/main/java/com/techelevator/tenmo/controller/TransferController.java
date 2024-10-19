package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class TransferController {

    private  final JdbcTransferDao transferDao;

    public TransferController(JdbcTransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @RequestMapping(path = "transfers/", method = RequestMethod.POST)
    public Transfer createTransfer(@Valid @RequestBody TransferDto transfer) {
        transferDao.transferType(transfer.getTransferType());
        transferDao.transferStatus("Send");
        transferDao.updateReceiverBalance(transfer);
        transferDao.updateSenderBalance(transfer);
        return transferDao.createTransfer(transfer);
    }

    @RequestMapping(path = "account/transfers", method = RequestMethod.GET)
    public List<Transfer> transfers(){
        return transferDao.retrieveTransfers();
    }

    @RequestMapping(path = "transfers/{transfer_id}", method = RequestMethod.GET)
    public Transfer retrieveTransfer(@Valid @PathVariable long transfer_id){
        return transferDao.retrieveTransferByID(transfer_id);
    }

}
