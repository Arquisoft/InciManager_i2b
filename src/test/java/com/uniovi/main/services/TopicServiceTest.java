package com.uniovi.main.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.TopicService;

@SpringBootTest(classes = { InciManagerI2bApplication.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class TopicServiceTest {
	
	@Autowired
	private TopicService topicsService;
	
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
		assertTrue(topicsService.getTopicsOf(inciSensor).size() == 3);
		assertTrue(topicsService.getTopicsOf(inciPerson).size() == 2);
		inciOperator.addProperty("Operators", "Operators");
		assertTrue(topicsService.getTopicsOf(inciOperator).size() == 3);
	}

	@Test
	public void testByTopics() {
		inciPerson.setLocation(new LatLng());
		assertTrue(topicsService.getTopicsOf(inciSensor).contains("geolocated"));
		assertTrue(topicsService.getTopicsOf(inciSensor).contains("sensor"));
	}

	@Test
	public void noValuesTest() {
		inciNoOperator.setAgent(new AgentInfo("Son", "pruebas", "123456"));
		inciNoLocation.setAgent(new AgentInfo("Son", "pruebas", "123456"));
		assertFalse(topicsService.getTopicsOf(inciNoOperator).contains("withOperator"));
		assertFalse(topicsService.getTopicsOf(inciNoLocation).contains("geolocated"));

	}

}
