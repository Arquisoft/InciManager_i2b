package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uniovi.services.IAgentsService;
import com.uniovi.services.IncidentsService;

@Controller
public class IncidentController {
	
	@Autowired
	@Qualifier("DefaultAgentsService")
	IAgentsService agentsService;
	
	@Autowired
	IncidentsService incidentsService;
	
	@RequestMapping(value="/incident/create", method=RequestMethod.POST)
	public void createIncident(@ModelAttribute Incident incident) {
		String username = incident.getUsername();
		String password = incident.getPassword();
		boolean existsAgent = agentsService.existsAgent(username, password);
		
		if (existsAgent) {
			incidentsService.sendToKafka(incident);
		} else {
			throw new AgentNotFoundException();
		}
	}

}
