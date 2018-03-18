package com.uniovi.main.entities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.json.IncidentJSONModule;

public class IncidentSerializerTest {
	
	private String incidentJson = "{\"agent\":{\"username\":\"agent1\",\"password\":\"pass-01\","
	      + "\"kind\":\"Person\"},\"inciName\":\"Mi first Incident\","
	      + "\"location\":{\"lat\":15.6,\"lon\":125.0},\"tags\":[\"fire\","
	      + "\"Person\"],\"moreInfo\":[\"myImage.jpg\"],\"properties\":{"
	      + "\"temperature\":\"50ºC\",\"priority\":1,\"operator\":\"Paco\"}}";

	  @Test
	  public void deserialize() throws Exception {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new IncidentJSONModule());
	 
	    Incident incident = objectMapper.readValue(incidentJson, Incident.class);
	 
	    assertEquals("agent1" , incident.getAgent().getUsername());
	    assertEquals("Person" , incident.getAgent().getKind());
	    assertEquals("Mi first Incident", incident.getInciName());
	    assertEquals(15.6, incident.getLocation().latitude, 0.01);
	    assertEquals(125, incident.getLocation().longitude, 0.01);
	    assertEquals(2, incident.getTags().size());
	    assertEquals("fire", incident.getTags().get(0));
	    assertEquals(1, incident.getMoreInfo().size());
	    assertEquals("myImage.jpg", incident.getMoreInfo().get(0));
	    assertEquals(3, incident.getProperties().size());
	    assertEquals(1, incident.getProperties().get("priority"));
	    assertEquals("50ºC", incident.getProperties().get("temperature"));
	    assertEquals("Paco", incident.getProperties().get("operator"));
	  }
	 
	  @Test
	  public void serialize() throws Exception {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new IncidentJSONModule());
	 
	    Incident incident = new Incident();
	    incident.setAgent(new AgentInfo("agent1", "pass-01", "Person"));
	    incident.setInciName("Mi first Incident");
	    incident.setLocation(new LatLng(15.6, 125));
	    incident.getTags().add("fire");
	    incident.getTags().add("Person");
	    incident.addMoreInfo("myImage.jpg");
	    incident.getProperties().put("priority", 1);
	    incident.getProperties().put("temperature", "50ºC");
	    incident.getProperties().put("operator", "Paco");
	 
	    String json = objectMapper.writeValueAsString(incident);
	    assertEquals(incidentJson, json);
	  }
}
