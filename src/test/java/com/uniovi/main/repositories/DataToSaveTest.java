package com.uniovi.main.repositories;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.json.JSONException;
import org.junit.Test;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.util.IncidentSelector;

public class DataToSaveTest {

	private AgentInfo agent1 = new AgentInfo("agent1", "pruebas123", "Person");
	private AgentInfo agent2 = new AgentInfo("agent2", "pruebas456", "Entity");
	private AgentInfo agent3 = new AgentInfo("agent3", "pruebas789", "Sensor");

	private IncidentSelector selector = new IncidentSelector();
	
	@Test
	public void testKindSelector() throws FileNotFoundException, JSONException {
		Incident incidentPerson = new Incident("inci1", new LatLng(124, 152), agent1);
		Incident incidentEntity = new Incident("inci2", new LatLng(37.5665, 126.9780), agent2);
		Incident incidentSensor = new Incident("inci3", new LatLng(15, 12), agent3);
		
		assertTrue(selector.isRelevant(incidentPerson));
		assertTrue(selector.isRelevant(incidentEntity));
		
		//Even though it is a sensor, it doesn't contain relevant information
		assertFalse(selector.isRelevant(incidentSensor));
	}
	
	@Test
	public void testPollutionSelector() throws FileNotFoundException, JSONException {
		Incident incidentPollution = new Incident("inci1", new LatLng(15, 12), agent3);
		incidentPollution.getProperties().put("pollution", 20.0);
		assertTrue(selector.isRelevant(incidentPollution));
		
		incidentPollution.getProperties().put("pollution", 56.0);
		assertFalse(selector.isRelevant(incidentPollution));
	}
	
	@Test
	public void testTemperatureSelector() throws FileNotFoundException, JSONException {
		Incident incidentTemperature = new Incident("incident", new LatLng(15,12), agent3);
		incidentTemperature.getProperties().put("temperature", 23.0);
		assertFalse(selector.isRelevant(incidentTemperature));
		
		incidentTemperature.getProperties().put("temperature", 35.0);
		assertTrue(selector.isRelevant(incidentTemperature));
	}
}
