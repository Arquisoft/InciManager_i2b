package com.uniovi.services;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DefaultAgentsService implements IAgentsService {

	@Override
	public boolean existsAgent(String username, String password) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		JSONObject request = new JSONObject();
		request.put("login", username);
        request.put("password", password);
		request.put("kind", "Person"); // TODO: ask about this next Monday

        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
        String url = "http://localhost:8080/agent";

        ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.POST, entity, String.class);
        HttpStatus responseCode = response.getStatusCode();
        return responseCode.equals(HttpStatus.OK);
	}

}
