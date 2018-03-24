package com.uniovi.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.entities.Operator;

@Service
public class InsertSampleDataService {

	@Autowired
	private IncidentsService incidentsService;

	@Autowired
	private AgentsService agentsService;

	@PostConstruct
	public void init() {
		Operator op1 = new Operator("operator1", "operator1", 0);
		Operator op2 = new Operator("operator2", "operator2", 0);
		Operator op3 = new Operator("operator3", "operator3", 0);
		
		AgentInfo agent1 = new AgentInfo("agent1", "pruebas123", "Person");
		AgentInfo agent2 = new AgentInfo("agent2", "pruebas456", "Entity");
		AgentInfo agent3 = new AgentInfo("agent3", "pruebas789", "Sensor");

		Incident incident1 = new Incident("inci1", new LatLng(124, 152), agent1);		
		Incident incident2 = new Incident("inci2", new LatLng(37.5665, 126.9780), agent2);
		Incident incident4 = new Incident("inci4", new LatLng(100, 200), agent1);

		Incident incident3 = new Incident("inci3", new LatLng(15, 12), agent3);
		incident3.getProperties().put("pollution", 20.0);

		Incident incident5 = new Incident("inci5", new LatLng(52, 42), agent3);
		incident5.getProperties().put("temperature", 35.0);

		incident1.assignOperator(op1);
		incident2.assignOperator(op2);
		incident3.assignOperator(op3);
		incident4.assignOperator(op1);
		incident5.assignOperator(op2);
		
		agentsService.addAgent(agent1);
		agentsService.addAgent(agent2);
		agentsService.addAgent(agent3);

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
		
		agentsService.deleteAgent(agentsService.findByUsername("agent1"));
		agentsService.deleteAgent(agentsService.findByUsername("agent2"));
		agentsService.deleteAgent(agentsService.findByUsername("agent3"));
	}

}
