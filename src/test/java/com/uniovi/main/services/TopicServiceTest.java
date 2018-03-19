package com.uniovi.main.services;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.services.TopicService;

public class TopicServiceTest 
{
	TopicService service = new TopicService();
	Incident inciPerson = new Incident("BrokenThing", new LatLng(42, 45),
			new AgentInfo("Alvaro","password","Person"));
	Incident inciSensor = new Incident("BrokenSensor", new LatLng(9, 34),
			new AgentInfo("Sensor1","sensor1","Sensor"));
	Incident inciOperator = new Incident("BrokenThingWithOperator", new LatLng(15, 37),
			new AgentInfo("Paco","Fernandez","Person"));
	
	@Test
	public void testListSize() {
		assertTrue(TopicService.getTopicsOf(inciSensor).size()==2);
		assertTrue(TopicService.getTopicsOf(inciPerson).size()==3);
		inciOperator.addProperty("Operators", "Operators");
		assertTrue(TopicService.getTopicsOf(inciOperator).size()==4);
	}

}
