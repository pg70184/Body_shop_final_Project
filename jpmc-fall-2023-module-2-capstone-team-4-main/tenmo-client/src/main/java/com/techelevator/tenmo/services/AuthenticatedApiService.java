package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class AuthenticatedApiService {

    protected String baseUrl;
    protected final RestTemplate restTemplate = new RestTemplate();

    protected String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    protected HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
