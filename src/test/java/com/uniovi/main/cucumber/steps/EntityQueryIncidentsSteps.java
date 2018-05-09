package com.uniovi.main.cucumber.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.manager.IncidentsInfoController;
import com.uniovi.services.IncidentsService;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SpringBootTest( classes=InciManagerI2bApplication.class )
public class EntityQueryIncidentsSteps {
    
    @Mock
    private IncidentsService incidentsService;

    @InjectMocks
    private IncidentsInfoController incidentController;

    private MockMvc mockMvc;
    private MvcResult result;
    private MockHttpSession session;
    
    private List<Incident> incidents;
    private AgentInfo entity;
    
	@Given("^an entity with username \"([^\"]*)\" and password \"([^\"]*)\"$")
    public void an_entity_with_username_and_password(String username, String password) {
		entity = new AgentInfo(username, password, "Entity");
    }
    
    @Given("^(\\d+) incidents belonging to the entity$")
    public void incidents_belonging_to_the_entity(int num_incidents) {
    		incidents = new ArrayList<Incident>();
    		for (int i = 0; i < num_incidents; i++) {
    			Incident inci = new Incident();
    			inci.setAgent(entity);
    			incidents.add(inci);
    		}

        MockitoAnnotations.initMocks(this);
    		when(incidentsService.getIncidentsByAgent(entity.getUsername())).thenReturn(incidents);
    		this.mockMvc = MockMvcBuilders.standaloneSetup(incidentController).build();
    }
    
    @When("^the entity logs in the system$")
    public void the_entity_logs_in_the_system() {
    		session = new MockHttpSession();
    		session.setAttribute("agentInfo", entity);
    }
    
    @When("^the entity accesses \"([^\"]*)\"$")
    public void the_entity_accesses(String url) throws Exception {
    		MockHttpServletRequestBuilder request = get(url).session(session);
    		result = mockMvc.perform(request).andReturn();
    }
    
    @Then("^the entity receives status code of (\\d+)$")
    public void the_entity_receives_status_code_of(int code) {
    		int receivedCode = result.getResponse().getStatus();
    		assertEquals(code, receivedCode);
    }
    
    @Then("^the entity can see its (\\d+) incidents$")
    public void the_entity_can_see_his_incidents(int numIncidents) {
    		@SuppressWarnings("unchecked")
		List<Incident> incidents = (List<Incident>) result.getModelAndView()
    				.getModel().get("incidentsList");
    		assertTrue(incidents.size() == numIncidents);
    }
}
