package com.uniovi.services;

import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.uniovi.entities.AgentInfo;
import com.uniovi.repositories.AgentsRepository;
import com.uniovi.repositories.MasterFileParser;

@Service
public class AgentsService {

	@Value("${agents_url}")
	private String agentsUrl;
	
	@Autowired
	private AgentsRepository agentsRepository;
	
	@Autowired
	private MasterFileParser fileParser;
	
	private static final Logger LOG = LoggerFactory.getLogger(AgentsService.class);

	public boolean existsAgent(AgentInfo agent) throws Exception {
		LOG.info("Sending POST request to url: {}", agentsUrl);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject request = new JSONObject();
		request.put("login", agent.getUsername());
        request.put("password", agent.getPassword());
		request.put("kind", agent.getKind());

        HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

        ResponseEntity<String> response = new RestTemplate().exchange(agentsUrl, HttpMethod.POST, entity, String.class);
        HttpStatus responseCode = response.getStatusCode();
        return responseCode.equals(HttpStatus.OK);
	}
	
	public AgentInfo findByUsername(String username) {
		return this.agentsRepository.findByUsername(username);
	}

	public void addAgent(AgentInfo agent) {
		this.agentsRepository.save(agent);
	}

	public void deleteAgent(AgentInfo agent) {
		this.agentsRepository.delete(agent);
	}
	
	public List<String> getAvailableKindNames() throws IOException {
		return fileParser.getKindNames();
	}
	

}
