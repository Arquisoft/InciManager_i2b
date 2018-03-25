package com.uniovi.main.services;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.services.InsertSampleDataService;
import com.uniovi.services.OperatorsService;

@SpringBootTest(classes = { InciManagerI2bApplication.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class InsertSampleDataServiceTest {

	@Autowired
	private IncidentsService incidentsService;
	
	@Autowired
	private AgentsService agentsService;
	
	@Autowired
	private InsertSampleDataService sampleDataService;

	@Autowired
	private OperatorsService operatorsService;
	
	@Test
	public void testSampleData() {
		/* we need to reinsert the data, as we are deleting/modifying it
		 * in other tests, so this test was not deterministic depending
		 * on the order of execution of the rest of the tests.
		 */
		incidentsService.deleteAll();
		agentsService.deleteAll();
		operatorsService.deleteAll();
		sampleDataService.init();
		
		//Start data inserted
		assertNotNull(incidentsService.getIncidentByName("inci1"));
		assertNotNull(incidentsService.getIncidentByName("inci2"));
		assertNotNull(incidentsService.getIncidentByName("inci3"));
		assertNotNull(incidentsService.getIncidentByName("inci4"));
		assertNotNull(incidentsService.getIncidentByName("inci5"));
		
		assertNotNull(agentsService.findByUsername("pacoo"));
		assertNotNull(agentsService.findByUsername("sonny"));
		assertNotNull(agentsService.findByUsername("agent3"));


	}

}
