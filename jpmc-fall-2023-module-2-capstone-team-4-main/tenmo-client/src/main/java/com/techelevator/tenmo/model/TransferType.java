package com.techelevator.tenmo.model;

public final class TransferType {

	public static final String REQUEST = "Request";
	public static final String SEND = "Send";	
		
	private TransferType() { } //private constructor prevents this class from being instantiated

	public static boolean isValid(String transferType) {
		return (REQUEST.equals(transferType)) || (SEND.equals(transferType));
	}
	
}
