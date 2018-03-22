package com.uniovi.main.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.services.InsertSampleDataService;

@SpringBootTest(classes = { InciManagerI2bApplication.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class InsertSampleDataServiceTest {

	/*
	 * Test the correct initialization of the sample data.
	 */
	@Autowired
	private InsertSampleDataService sampleDataService;
	
	@Autowired
	private IncidentsService incidentsService;
	
	@Autowired
	private AgentsService agentsService;

	@Test
	public void testSampleData() {
		
		//Start data already inserted
		assertNotNull(incidentsService.getIncidentByName("inci1"));
		assertNotNull(incidentsService.getIncidentByName("inci2"));
		assertNotNull(incidentsService.getIncidentByName("inci3"));
		assertNotNull(incidentsService.getIncidentByName("inci4"));
		assertNotNull(incidentsService.getIncidentByName("inci5"));
		
		assertNotNull(agentsService.findByUsername("agent1"));
		assertNotNull(agentsService.findByUsername("agent2"));
		assertNotNull(agentsService.findByUsername("agent3"));
		
		sampleDataService.finalize();
		
		assertNull(incidentsService.getIncidentByName("inci1"));
		assertNull(incidentsService.getIncidentByName("inci2"));
		assertNull(incidentsService.getIncidentByName("inci3"));
		assertNull(incidentsService.getIncidentByName("inci4"));
		assertNull(incidentsService.getIncidentByName("inci5"));
		
		assertNull(agentsService.findByUsername("agent1"));
		assertNull(agentsService.findByUsername("agent2"));
		assertNull(agentsService.findByUsername("agent3"));

	}

}
