package com.uniovi.main.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;

@SpringBootTest(classes = { InciManagerI2bApplication.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class AgentsServiceTest {

	@Autowired
	private AgentsService agentsService;


	private AgentInfo testInfo1;
	private AgentInfo testInfo2;


	@After
	public void clean() {
		if (testInfo1 != null)
			agentsService.deleteAgent(testInfo1);
		if (testInfo2 != null)
			agentsService.deleteAgent(testInfo2);

	}

	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void testAddNullAgent() {
		agentsService.addAgent(testInfo1);
	}

	@Test
	public void testCRUDAgent() {

		testInfo1 = new AgentInfo("agentTest1", "pruebas123", "Person");
		testInfo2 = new AgentInfo("agentTest2", "pruebas123", "Sensor");

		agentsService.addAgent(testInfo1);
		assertEquals(testInfo1, agentsService.findByUsername(testInfo1.getUsername()));
		assertEquals(null, agentsService.findByUsername(testInfo2.getUsername()));

		agentsService.addAgent(testInfo2);
		agentsService.deleteAgent(testInfo1);
		assertEquals(testInfo2, agentsService.findByUsername(testInfo2.getUsername()));
		assertEquals(null, agentsService.findByUsername(testInfo1.getUsername()));

	}

	@Test (expected = Exception.class) //Agents module not open
	public void testExistsAgent() throws Exception {

		testInfo1 = new AgentInfo("agentTest1", "pruebas123", "Person");
		testInfo2 = new AgentInfo("agentTest2", "pruebas123", "Sensor");
		agentsService.addAgent(testInfo1);
		agentsService.addAgent(testInfo2);

		assertTrue(agentsService.existsAgent(testInfo1));
		assertTrue(agentsService.existsAgent(testInfo2));

	}

}
