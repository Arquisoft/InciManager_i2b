package com.uniovi.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;

@Service
public class InsertSampleDataService {
	
	@Autowired
	private IncidentsService incidentsService;
	
	@PostConstruct
	public void init() {
		AgentInfo agent1 = new AgentInfo("agent1", "pruebas123", "Person");
		AgentInfo agent2 = new AgentInfo("agent2", "pruebas456", "Entity");
		AgentInfo agent3 = new AgentInfo("agent3", "pruebas789", "Sensor");
		
		Incident incident1 = new Incident("inci1", "location1");
		Incident incident2 = new Incident("inci2", "location2");
		Incident incident3 = new Incident("inci3", "location3");
		Incident incident4 = new Incident("inci4", "location4");
		Incident incident5 = new Incident("inci5", "location5");

		incident1.setAgent(agent1);
		incident2.setAgent(agent2);
		incident3.setAgent(agent3);
		incident4.setAgent(agent1);
		incident5.setAgent(agent3);
		
		incidentsService.addIncident(incident1);
		incidentsService.addIncident(incident2);
		incidentsService.addIncident(incident3);
		incidentsService.addIncident(incident4);
		incidentsService.addIncident(incident5);

	}
	
	@PreDestroy
	public void finalize() {
		incidentsService.deleteIncidentByName("inci1");
		incidentsService.deleteIncidentByName("inci2");
		incidentsService.deleteIncidentByName("inci3");
		incidentsService.deleteIncidentByName("inci4");
		incidentsService.deleteIncidentByName("inci5");
		incidentsService.deleteIncidentByName("inci6");
	}

}
