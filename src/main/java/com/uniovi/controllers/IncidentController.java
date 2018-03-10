package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uniovi.entities.Incident;
import com.uniovi.services.IAgentsService;
import com.uniovi.services.KafkaService;
import com.uniovi.util.exception.AgentNotFoundException;

@Controller
public class IncidentController {
	
	@Autowired
	@Qualifier("DefaultAgentsService")
	IAgentsService agentsService;
	
	@Autowired
	KafkaService kafkaService;
	
	@RequestMapping(value="/incident/create", method=RequestMethod.POST)
	public void createIncident(@ModelAttribute Incident incident) {
		String username = incident.getUsername();
		String password = incident.getPassword();
		boolean existsAgent = agentsService.existsAgent(username, password);
		
		if (existsAgent) {
			kafkaService.sendToKafka(incident);
		} else {
			throw new AgentNotFoundException();
		}
	}

}
