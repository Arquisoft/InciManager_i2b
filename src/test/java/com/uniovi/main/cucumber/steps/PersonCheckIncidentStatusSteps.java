package com.uniovi.main.cucumber.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import com.uniovi.entities.IncidentState;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.manager.IncidentsInfoController;
import com.uniovi.services.IncidentsService;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SpringBootTest(classes = InciManagerI2bApplication.class)
public class PersonCheckIncidentStatusSteps {

	@Mock
	private IncidentsService incidentsService;

	@InjectMocks
	private IncidentsInfoController incidentController;

	private MockMvc mockMvc;
	private MvcResult result;
	private MockHttpSession session;

	private List<Incident> incidents;
	private AgentInfo person;
	
	private int checkedIncident;

	@Given("^a person with name \"([^\"]*)\" and password \"([^\"]*)\"$")
	public void a_person_with_username_and_password(String username, String password) {
		person = new AgentInfo(username, password, "Person");
	}

	@Given("^(\\d+) unassigned incidents belonging to the person$")
	public void incidents_belonging_to_the_person(int num_incidents) {
		incidents = new ArrayList<Incident>();
		for (int i = 0; i < num_incidents; i++) {
			// No state, unassigned by default
			Incident inci = new Incident();
			inci.setAgent(person);
			incidents.add(inci);
		}

		MockitoAnnotations.initMocks(this);
		when(incidentsService.getIncidentsByAgent(person.getUsername())).thenReturn(incidents);
		this.mockMvc = MockMvcBuilders.standaloneSetup(incidentController).build();
	}

	@When("^the person enters the system$")
	public void the_person_enters_the_system() {
		session = new MockHttpSession();
		session.setAttribute("agentInfo", person);
	}

	@When("^the person accesses  \"([^\"]*)\"$")
	public void the_person_accesses(String url) throws Exception {
		MockHttpServletRequestBuilder request = get(url).session(session);
		result = mockMvc.perform(request).andReturn();
	}

	@Then("^the person receives a status code of (\\d+)$")
	public void the_person_receives_status_code_of(int code) {
		int receivedCode = result.getResponse().getStatus();
		assertEquals(code, receivedCode);
	}

	@Then("^the person can see the incident status$")
	public void the_person_can_see_its_incidents_status() {
		@SuppressWarnings("unchecked")
		List<Incident> incidents = (List<Incident>) result.getModelAndView()
			.getModel().get("incidentsList");
		// Incidents are unassigned
		for (Incident i : incidents) {
			assertNull(i.getState());
		}
	}

	@Then("^the incident number (\\d+) is opened$")
	public void the_incident_is_opened(int incident_number) {
		
		checkedIncident = incident_number;

		assertTrue(incident_number < incidents.size());
		incidents.get(incident_number - 1).setState(IncidentState.OPEN);
	}

	@Then("^the person can refresh \"([^\"]*)\"$")
	public void the_person_can_refresh(String url) throws Exception {
		MockHttpServletRequestBuilder request = get(url).session(session);
		result = mockMvc.perform(request).andReturn();
	}

	@Then("^the person can see the new status of his incident")
	public void person_can_see_new_status_of_his_incident() {

		@SuppressWarnings("unchecked")
		List<Incident> incidents = (List<Incident>) result.getModelAndView()
			.getModel().get("incidentsList");
		// Incidents are unassigned
		for (int i = 0; i< incidents.size(); i++) {
			if (i == checkedIncident-1)
				assertTrue(incidents.get(i).getState() == IncidentState.OPEN);
			else
				assertNull(incidents.get(i).getState());
		}

	}

}
