package com.uniovi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.util.exception.AgentNotFoundException;

@RestController
public class IncidentsInfoController {

	@Autowired
	private AgentsService agentsService;

	@Autowired
	private IncidentsService incidentsService;

	@RequestMapping(value = "/incidents/info", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<Incident> getIncidentsInfoJSON(@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "password", required = true) String password,
			@RequestParam(name = "kind", required = false) String kind) throws Exception {
		
		AgentInfo ainfo = new AgentInfo(username, password, kind);
		if (!agentsService.existsAgent(ainfo)) {
			throw new AgentNotFoundException();
		}

		List<Incident> agentIncidents = incidentsService.getIncidentsByAgent(ainfo);

		return agentIncidents;

	}

}
