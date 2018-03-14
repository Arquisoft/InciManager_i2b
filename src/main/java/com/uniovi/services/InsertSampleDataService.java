package com.uniovi.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;

@Service
public class InsertSampleDataService {
	
	@Autowired
	private AgentsService agentsService;
	
	private AgentInfo agent1;
	private AgentInfo agent2;
	private AgentInfo agent3;
	
	@PostConstruct
	@Transactional
	public void init() {
		agent1 = new AgentInfo("agent1", "pruebas123", "Person");
		agent2 = new AgentInfo("agent2", "pruebas456", "Entity");
		agent3 = new AgentInfo("agent3", "pruebas789", "Sensor");
		
		Incident incident1 = new Incident("inci1", "location1");
		Incident incident2 = new Incident("inci2", "location2");
		Incident incident3 = new Incident("inci3", "location3");
		Incident incident4 = new Incident("inci4", "location4");
		Incident incident5 = new Incident("inci5", "location5");
		
		agent1.addIncident(incident1);
		agent1.addIncident(incident4);
		agent2.addIncident(incident2);
		agent3.addIncident(incident3);
		agent3.addIncident(incident5);
		
		agentsService.addAgent(agent1);
		agentsService.addAgent(agent2);
		agentsService.addAgent(agent3);
	}
	
	@PreDestroy
	public void finalize() {
		agentsService.deleteAgent(agent1);
		agentsService.deleteAgent(agent2);
		agentsService.deleteAgent(agent3);
	}

}
