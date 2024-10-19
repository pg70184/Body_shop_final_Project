package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.model.Transfer;

public class AccountService extends AuthenticatedApiService {

	public AccountService(String baseUrl) {
		this.baseUrl = baseUrl + "account/";
	}

	public BigDecimal getBalance(int userID) {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(baseUrl + userID, HttpMethod.GET,
                            makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
		return balance;
	}

	public Transfer[] retrieveAllTransfers() {
        Transfer[] transfers = null;
        try {
    		ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(baseUrl +"transfers", HttpMethod.GET,
                            makeAuthEntity(), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
		return transfers;
	}

}
