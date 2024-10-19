package com.techelevator.tenmo.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Component
public class TransferDto {

	@NotNull
	private long userFrom;
	@NotNull
	private long userTo;
	@Positive
	private BigDecimal amount;
	@NotNull
	private String transferType;


	public long getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(long userFrom) {
		this.userFrom = userFrom;
	}

	public long getUserTo() {
		return userTo;
	}

	public void setUserTo(long userTo) {
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


}
