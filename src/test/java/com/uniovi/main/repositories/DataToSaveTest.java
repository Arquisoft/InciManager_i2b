package com.uniovi.main.repositories;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.util.IncidentSelector;

public class DataToSaveTest {

	AgentInfo agent1 = new AgentInfo("agent1", "pruebas123", "Person");
	AgentInfo agent2 = new AgentInfo("agent2", "pruebas456", "Entity");
	AgentInfo agent3 = new AgentInfo("agent3", "pruebas789", "Sensor");
	
	Incident incidentPerson = new Incident("inci1", new LatLng(124, 152), agent1);
	Incident incidentEntity = new Incident("inci2", new LatLng(37.5665, 126.9780), agent2);
	Incident incidentSensor = new Incident("inci3", new LatLng(15, 12), agent3);
	Incident incidentSensor2 = new Incident("inci4", new LatLng(100, 200), agent1);
	Incident incidentSensor3 = new Incident("inci5", new LatLng(52, 42), agent3);

	IncidentSelector selector = new IncidentSelector();
	
	@Test
	public void testKindSelector() {
		assertTrue(selector.isRelevant(incidentPerson));
		assertTrue(selector.isRelevant(incidentEntity));
	}
}
