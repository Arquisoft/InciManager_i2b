package com.uniovi.main.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.services.TopicService;

public class TopicServiceTest {
	private Incident inciPerson = new Incident("BrokenThing", new LatLng(42, 45),
			new AgentInfo("Alvaro", "password", "Person"));
	private Incident inciSensor = new Incident("BrokenSensor", new LatLng(9, 34),
			new AgentInfo("Sensor1", "sensor1", "Sensor"));
	private Incident inciOperator = new Incident("BrokenThingWithOperator", new LatLng(15, 37),
			new AgentInfo("Paco", "Fernandez", "Person"));

	private Incident inciNoOperator = new Incident("BrokenThingWithOperator", new LatLng(15, 37));
	private Incident inciNoLocation = new Incident();

	@Test
	public void testListSize() {
		assertTrue(TopicService.getTopicsOf(inciSensor).size() == 3);
		assertTrue(TopicService.getTopicsOf(inciPerson).size() == 2);
		inciOperator.addProperty("Operators", "Operators");
		assertTrue(TopicService.getTopicsOf(inciOperator).size() == 3);
	}

	@Test
	public void testByTopics() {
		inciPerson.setLocation(new LatLng());
		assertTrue(TopicService.getTopicsOf(inciSensor).contains("geolocated"));
		assertTrue(TopicService.getTopicsOf(inciSensor).contains("sensor"));
	}

	@Test (expected = NullPointerException.class)
	public void noValuesrTest() {
		assertFalse(TopicService.getTopicsOf(inciNoOperator).contains("withOperator"));
		assertFalse(TopicService.getTopicsOf(inciNoLocation).contains("geolocated"));

	}

}
