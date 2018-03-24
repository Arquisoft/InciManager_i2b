package com.uniovi.main.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;

@SpringBootTest(classes = { InciManagerI2bApplication.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class IncidentsServiceTest {

	@Autowired
	private IncidentsService incidentsService;

	@Autowired
	private AgentsService agentsService;
	
	private AgentInfo testInfo1;
	private AgentInfo testInfo2;
	private AgentInfo testInfo3;
	
	@After
	public void clean() {
		if (testInfo1 != null)
			agentsService.deleteAgent(testInfo1);
		if (testInfo2 != null)
			agentsService.deleteAgent(testInfo2);
		if (testInfo3 != null)
			agentsService.deleteAgent(testInfo3);

	}

	@Test
	public void testIncidentsAccess() {

		// Create, read incidents
		testInfo1 = new AgentInfo("agentTest1", "pruebas123", "Person");
		testInfo2 = new AgentInfo("agentTest2", "pruebas123", "Person");
		testInfo3 = new AgentInfo("agentTest3", "pruebas123", "Sensor");


		agentsService.addAgent(testInfo1);
		agentsService.addAgent(testInfo2);
		agentsService.addAgent(testInfo3);

		Incident inciTest1 = new Incident("inciTest1", new LatLng(10, 12), testInfo1);
		inciTest1.getProperties().put("pollution", 15.0);

		Incident inciTest2 = new Incident("inciTest2", new LatLng(52, 42), testInfo2);
		inciTest2.getProperties().put("temperature", 29.3);
		
		Incident inciTest3 = new Incident("inciTest3", new LatLng(25, 25), testInfo3);


		incidentsService.addNewIncident(inciTest1);

		assertEquals(incidentsService.getIncidentByName("inciTest1"), inciTest1);
		assertEquals(incidentsService.getIncidentByName("inciTest2"), null);

		incidentsService.addNewIncident(inciTest2);

		assertEquals(incidentsService.getIncidentByName("inciTest2"), inciTest2);

		assertTrue(incidentsService.getIncidentsByAgent(testInfo1.getUsername()).contains(inciTest1));
		assertTrue(incidentsService.getIncidentsByAgent(testInfo2.getUsername()).contains(inciTest2));
		assertFalse(incidentsService.getIncidentsByAgent(testInfo1.getUsername()).contains(inciTest2));
		assertFalse(incidentsService.getIncidentsByAgent(testInfo2.getUsername()).contains(inciTest1));
		
		incidentsService.addNewIncident(inciTest3);
		//Sensor non-special value not stored
		assertEquals(incidentsService.getIncidentByName("inciTest3"), null);


		// Incident was added as OPEN
		assertTrue(incidentsService.getIncidentByName("inciTest1").getState().name().equalsIgnoreCase("Open"));

		// Delete incidents
		// By name
		incidentsService.deleteIncidentByName("inciTest1");
		assertEquals(incidentsService.getIncidentByName("inciTest1"), null);
		assertEquals(incidentsService.getIncidentByName("inciTest2"), inciTest2);

		// By id
		ObjectId id = incidentsService.getIncidentByName("inciTest2").getId();
		incidentsService.deleteIncidentById(id);
		assertEquals(incidentsService.getIncidentByName("inciTest2"), null);

	}

}
