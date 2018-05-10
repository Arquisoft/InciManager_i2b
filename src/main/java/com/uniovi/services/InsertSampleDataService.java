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

	@Autowired
	private OperatorsService operatorsService;
	
	@PostConstruct
	public void init() {
		Operator op = new Operator("operator11@dashboard.com", "op1", false);
		Operator op2 = new Operator("operator22@dashboard.com", "op2", false);
		Operator op3 = new Operator("operator33@dashboard.com", "op3",false);
		
		AgentInfo agent1 = new AgentInfo("pacoo", "123456", "Person");
		AgentInfo agent2 = new AgentInfo("sonny", "pass123", "Entity");
		AgentInfo agent3 = new AgentInfo("agent3", "pruebas789", "Sensor");
		AgentInfo agent4 = new AgentInfo("agent4", "aaaaaaaa", "Sensor");

		Incident incident1 = new Incident("inci1", new LatLng(124, 152), agent1);
		incident1.addTag("fire").addTag("important");
		Incident incident2 = new Incident("inci2", new LatLng(37.5665, 126.9780), agent2);
		incident2.addTag("earthquake").addTag("unassigned");
		Incident incident3 = new Incident("inci3", new LatLng(15, 12), agent3);
		incident3.getProperties().put("pollution", "20.0");
		Incident incident4 = new Incident("inci4", new LatLng(100, 200), agent1);
		Incident incident5 = new Incident("inci5", new LatLng(52, 42), agent3);
		incident5.getProperties().put("temperature", "35.0");

		operatorsService.addOperator(op);
		operatorsService.addOperator(op2);
		operatorsService.addOperator(op3);
		
		agentsService.addAgent(agent1);
		agentsService.addAgent(agent2);
		agentsService.addAgent(agent3);
		agentsService.addAgent(agent4);

		incidentsService.addIncident(incident1);
		incidentsService.addIncident(incident2);
		incidentsService.addIncident(incident3);
		incidentsService.addIncident(incident4);
		incidentsService.addIncident(incident5);
	}

	@PreDestroy
	public void finalize() {
		incidentsService.deleteAll();
		agentsService.deleteAll();
		operatorsService.deleteAll();
	}

}
