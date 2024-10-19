package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;

import java.util.List;


public interface TransferDao {
    int transferType(String transferType);

    int transferStatus(String transferStatus);
    Transfer createTransfer(TransferDto transfer);

    List<Transfer> retrieveTransfers();
    Transfer retrieveTransferByID(Long transferID);
}
