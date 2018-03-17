package com.uniovi.main.repositories;

import static org.junit.Assert.assertFalse;
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

	IncidentSelector selector = new IncidentSelector();
	
	@Test
	public void testKindSelector() {
		Incident incidentPerson = new Incident("inci1", new LatLng(124, 152), agent1);
		Incident incidentEntity = new Incident("inci2", new LatLng(37.5665, 126.9780), agent2);
		Incident incidentSensor = new Incident("inci3", new LatLng(15, 12), agent3);
		
		assertTrue(selector.isRelevant(incidentPerson));
		assertTrue(selector.isRelevant(incidentEntity));
		
		//Even though it is a sensor, its tags don't contain 'temperature' or 'pollution'
		assertFalse(selector.isRelevant(incidentSensor));
	}
	
	@Test
	public void testPollutionSelector() {
		Incident incidentPollution = new Incident("inci1", new LatLng(15, 12), agent3);
		incidentPollution.getTags().add("pollution");
		incidentPollution.getProperties().put("value", 20.0);
		assertTrue(selector.isRelevant(incidentPollution));
		
		incidentPollution.getProperties().put("value", 56.0);
		assertFalse(selector.isRelevant(incidentPollution));
	}
	
	@Test
	public void testTemperatureSelector() {
		Incident incidentTemperature = new Incident("incident", new LatLng(15,12), agent3);
		incidentTemperature.getTags().add("temperature");
		incidentTemperature.getProperties().put("value", 23.0);
		assertFalse(selector.isRelevant(incidentTemperature));
		
		incidentTemperature.getProperties().put("value", 35.0);
		assertTrue(selector.isRelevant(incidentTemperature));
	}
}
