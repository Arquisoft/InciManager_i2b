package com.uniovi.main.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class IncidentAccessTest {

    @Autowired
    private IncidentsService incidentsService;

    @Autowired
    private AgentsService agentsService;


    @Test
    public void t01testReadIncident() throws Exception {
    		AgentInfo agent1 = agentsService.findByUsername("pacoo");

		List<Incident> incidentsUser1 = incidentsService.getIncidentsByAgent(agent1.getUsername());
		Collections.sort(incidentsUser1, (a, b) -> a.getInciName().compareTo(b.getInciName()));

		assertEquals(2, incidentsUser1.size());
		Incident inci1 = incidentsUser1.get(0);

		assertEquals(agent1, inci1.getAgent());
		assertEquals("inci1", inci1.getInciName());
		assertEquals(124, inci1.getLocation().latitude, 0.01);
		assertEquals(152, inci1.getLocation().longitude, 0.01);
		assertEquals(0, inci1.getProperties().size());

		//Add a second incident to agent1
		Incident incident6 = new Incident("inci6", new LatLng(155, 42), agent1);
		incidentsService.addIncident(incident6);

		incidentsUser1 = incidentsService.getIncidentsByAgent(agent1.getUsername());
		assertEquals(3, incidentsUser1.size());
    }

    @Test
    public void t02testDeleteIncident() throws Exception {
		List<Incident> incidents = incidentsService.getIncidents();
		assertEquals(6, incidents.size());

		incidentsService.deleteIncidentByName("inci2");
		incidents = incidentsService.getIncidents();
		assertEquals(5, incidents.size());

		//Agent 4 no incidents
		AgentInfo agent4 = agentsService.findByUsername("agent4");
		incidents = incidentsService.getIncidentsByAgent(agent4.getUsername());
		assertEquals(0, incidents.size());

		AgentInfo agent1 = agentsService.findByUsername("pacoo");
		incidentsService.deleteIncidentByName("inci1");
		incidents = incidentsService.getIncidentsByAgent(agent1.getUsername());

		assertEquals(2, incidents.size());
		assertEquals("pacoo", incidents.get(0).getAgent().getUsername());
		assertEquals("inci4", incidents.get(0).getInciName());
    }


}
