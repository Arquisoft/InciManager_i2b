package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.services.KafkaService;
import com.uniovi.util.exception.AgentNotFoundException;

@Controller
public class IncidentController {
	
	@Autowired
	AgentsService agentsService;
	
	@Autowired
	IncidentsService incidentsService;
	
	@Autowired
	KafkaService kafkaService;
	
	@RequestMapping(value="/incident/create", method=RequestMethod.POST)
	@ResponseBody
	public String createIncident(@RequestBody Incident incident, @RequestBody AgentInfo agent) throws Exception {
		if (!agentsService.existsAgent(agent)) {
			throw new AgentNotFoundException();
		}
		
		agentsService.addAgent(agent);
		incidentsService.addIncident(incident);
		kafkaService.sendToKafka(incident);
		return "Incident correctly sent!";
	}

}
