package com.uniovi.main.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.uniovi.controllers.IncidentsInfoController;
import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;

@SpringBootTest(classes = { InciManagerI2bApplication.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class IncidentsInfoControllerTest {

	@Mock
	private AgentsService agentsService;

	@Mock
	public IncidentsService incidentsService;

	@InjectMocks
	private IncidentsInfoController incidentsInfoController;

	private AgentInfo testInfo;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		testInfo = new AgentInfo("Ejemplo", "pass", "Person");
		try {
			when(agentsService.existsAgent(testInfo)).thenReturn(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		when(agentsService.findByUsername(testInfo.getUsername())).thenReturn(testInfo);

		// Test incidents, list to be returned
		List<Incident> testIncidents = new ArrayList<Incident>();
		testIncidents.add(new Incident("testIncident0", new LatLng(1.0, 1.0), testInfo));

		when(incidentsService.getIncidentsByAgent(testInfo)).thenReturn(testIncidents);

		this.mockMvc = MockMvcBuilders.standaloneSetup(incidentsInfoController).build();

	}

	@Test
	public void testIncidentsInfoRequestSuccesfull() throws Exception {

		MockHttpServletRequestBuilder request = post("/incidentsinfo").accept(MediaType.APPLICATION_JSON_VALUE)
				.param("username", "Ejemplo").param("password", "pass").contentType(MediaType.APPLICATION_JSON);

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		// Succesfull request
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}

	/*
	 * Send request with a couple of incidents to be returned, compared real and
	 * expected output
	 */
	@Test
	public void testIncidentsInfoContentOK() throws Exception {

		MockHttpServletRequestBuilder request = post("/incidentsinfo").accept(MediaType.APPLICATION_JSON_VALUE)
				.param("username", "Ejemplo").param("password", "pass").contentType(MediaType.APPLICATION_JSON);

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		// Succesfull request
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		// Correct content returned
		assertEquals(response.getContentAsString(),
				"[{\"agent\":{\"username\":\"" + testInfo.getUsername() + "\"," + "\"password\":\""
						+ testInfo.getPassword() + "\",\"kind\":\"" + testInfo.getKind() + "\"},\"inciName\""
						+ ":\"testIncident0\",\"location\":{\"lat\":1.0,\"lon\":1.0},\"tags\""
						+ ":[],\"moreInfo\":[],\"properties\":{}}]");

	}

	/*
	 * Send request with a non-valid agent, check NOT FOUND status of response
	 */
	@Test
	public void testIncidentsInfoNotFound() throws Exception {

		MockHttpServletRequestBuilder request = post("/incidentsinfo").accept(MediaType.APPLICATION_JSON_VALUE)
				.param("username", "notAnAgent").param("password", "fail").param("kind", "Person")
				.contentType(MediaType.APPLICATION_JSON);

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

	}

	/*
	 * Send get request to html form
	 */
	@Test
	public void testIncidentsInfoGetForm() throws Exception {

		MockHttpServletRequestBuilder request = get("/agentform").param("error", "true");

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

	/*
	 * Send get request to html form with the error flagged
	 */
	@Test
	public void testIncidentsInfoGetFormError() throws Exception {

		MockHttpServletRequestBuilder request = get("/agentform").param("error", "true");

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

	/*
	 * Send poat request after login with a valid agent
	 */
	@Test
	public void testIncidentsInfoPost() throws Exception {

		MockHttpServletRequestBuilder request = post("/agentform").param("username", "Ejemplo")
				.param("password", "pass");

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		//Accepted post
		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

	/*
	 * Send poat request after login with a non valid agent
	 */
	@Test
	public void testIncidentsInfoPostNonExists() throws Exception {

		MockHttpServletRequestBuilder request = post("/agentform").param("username", "badExample")
				.param("password", "fail").param("kind", "Person");

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		// Redirected to Login
		assertEquals(HttpStatus.FOUND.value(), response.getStatus());

	}

}
