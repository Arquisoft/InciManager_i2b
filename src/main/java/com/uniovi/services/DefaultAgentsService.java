package com.uniovi.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class DefaultAgentsService implements IAgentsService {

	@Override
	public boolean existsAgent(String username, String password) {
		MultiValueMap<String, String> headers = prepareHeaders();

        Map<String, String> payload = new HashMap<String, String>();
        payload.put("username", username);
        payload.put("password", password);

        HttpEntity<?> request = new HttpEntity<>(payload, headers);
        String url = "http://localhost:8080/";

        ResponseEntity<?> response = new RestTemplate().postForEntity(url, request, String.class);
        HttpStatus responseCode = response.getStatusCode();
        return responseCode.equals(HttpStatus.OK);
	}
	
	private MultiValueMap<String, String> prepareHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        headers.setAll(map);
        return headers;
	}

}
