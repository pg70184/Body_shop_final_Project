package com.techelevator.tenmo.services;

import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.TransferStatus;

public class TransferService extends AuthenticatedApiService {

	public TransferService(String baseUrl) {
		this.baseUrl = baseUrl + "transfers/";
	}
	
	public Transfer createTransfer(TransferDto dto) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(baseUrl, HttpMethod.POST, makeTransferDtoEntity(dto), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
		return transfer;
	}

	public Transfer updatePendingTransferStatus(int transferId, String status) {
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(baseUrl + transferId, HttpMethod.PUT,
                            makeTransferStatusEntity(status), Transfer.class);
            transfer = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
		return transfer;
	}

	public Transfer retrieveTransferDetails(Integer transferId) {
	    Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(baseUrl + transferId, HttpMethod.GET,
                            makeAuthEntity(), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
	}

    private HttpEntity<TransferDto> makeTransferDtoEntity(TransferDto transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<TransferStatusUpdateDTO> makeTransferStatusEntity(String status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(new TransferStatusUpdateDTO(status), headers);
    }

    private static class TransferStatusUpdateDTO {
        private String transferStatus;

        public TransferStatusUpdateDTO(String transferStatus) {
            if(TransferStatus.isValid(transferStatus)) {
                this.transferStatus = transferStatus;
            } else {
                throw new IllegalArgumentException("Invalid transferStatus: "+transferStatus);
            }
        }

        public String getTransferStatus() {
            return transferStatus;
        }

        public void setTransferStatus(String transferStatus) {
            this.transferStatus = transferStatus;
        }
    }
}

