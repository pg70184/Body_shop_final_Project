package com.techelevator.tenmo.services;

import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.model.User;

public class UserService extends AuthenticatedApiService {

	public UserService(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public User[] retrieveAllUsers() {
        User[] users = null;
        try {

            ResponseEntity<User[]> response =
                    restTemplate.exchange(baseUrl + "user", HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
		return users;
	}
}
