package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Transfer {
    @NotNull
    private Long transferId;
    @NotBlank
    private User userFrom;
    @NotBlank
    private User userTo;
    @Positive(message = "cannot be negative amounts")
    private BigDecimal amount;
    @NotBlank(message = "must have valid transfer type")
    private String transferType;
    @NotBlank
    private String transferStatus;


    public Transfer(){

    }

    public Transfer(Long transferId, User userFrom, User userTo, BigDecimal amount, String transferType, String transferStatus) {
        this.transferId = transferId;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.amount = amount;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
    }


    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }
}
