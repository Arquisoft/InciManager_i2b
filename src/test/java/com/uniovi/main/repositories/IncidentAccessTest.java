package com.uniovi.main.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.services.InsertSampleDataService;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class IncidentAccessTest {
    
    @Autowired
	public InsertSampleDataService sampleDataService;
    
    @Autowired
    private IncidentsService incidentsService;
    
    @Autowired
    private AgentsService agentsService;
    
    @Before
    public void setup() throws Exception {
    }

    @Test
    public void T01testInsertIncident() throws Exception {
    		//Check if the SampleDataService insertions worked.
		assertEquals(5, incidentsService.getIncidents().size());
    }
    
    @Test
    public void T02testReadIncident() throws Exception {
    		AgentInfo agent1 = agentsService.findByUsername("agent1"); 
    		
		List<Incident> incidentsUser1 = incidentsService.getIncidentsByAgent(agent1);
		Collections.sort(incidentsUser1, (a, b) -> a.getInciName().compareTo(b.getInciName()));
		
		assertEquals(2, incidentsUser1.size());
		Incident inci1 = incidentsUser1.get(0);
		
		assertEquals(agent1, inci1.getAgent());
		assertEquals("inci1", inci1.getInciName());
		assertEquals("location1", inci1.getLocation());
		assertEquals(0, inci1.getProperties().size());
		
		//Add a second incident to agent1
		Incident incident6 = new Incident("inci6", "location1");
		agent1.addIncident(incident6);
		agentsService.addAgent(agent1);
		
		incidentsUser1 = incidentsService.getIncidentsByAgent(agent1);
		assertEquals(3, incidentsUser1.size());
    }
    
    @Test
    @Transactional
    public void T03testDeleteIncident() throws Exception {
		List<Incident> incidents = incidentsService.getIncidents();
		assertEquals(6, incidents.size());

		AgentInfo agent2 = agentsService.findByUsername("agent2"); 
		Incident incident2 = incidentsService.getIncidentByName("inci2");
		
		agent2.removeIncident(incident2);
		agentsService.addAgent(agent2);
		incidentsService.deleteIncidentByName("inci2");
		incidents = incidentsService.getIncidents();
		assertEquals(5, incidents.size());
		
		//Agent 4 no incidents
		AgentInfo agent4 = agentsService.findByUsername("agent4");
		incidents = incidentsService.getIncidentsByAgent(agent4);
		assertEquals(0, incidents.size());
		
		AgentInfo agent1 = agentsService.findByUsername("agent1");
		Incident incident1 = incidentsService.getIncidentByName("inci1");
		agent1.removeIncident(incident1);
		incidentsService.deleteIncidentByName("inci1");
		incidents = incidentsService.getIncidentsByAgent(agent1);
		assertEquals(2, incidents.size());
		
		
		incidents = incidentsService.getIncidentsByAgent(agent1);
		assertEquals("agent1", incidents.get(0).getAgent().getUsername());
		assertEquals("inci4", incidents.get(0).getInciName());
    }

  
}
