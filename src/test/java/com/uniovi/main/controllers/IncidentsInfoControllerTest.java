package com.uniovi.main.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.manager.IncidentsInfoController;
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
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testInfo = new AgentInfo("Ejemplo", "pass", "Person");

		when(agentsService.existsAgent(testInfo)).thenReturn(true);
		when(agentsService.findByUsername(testInfo.getUsername())).thenReturn(testInfo);

		// Test incidents, list to be returned
		List<Incident> testIncidents = new ArrayList<Incident>();
		testIncidents.add(new Incident("testIncident0", new LatLng(1.0, 1.0), testInfo));

		when(incidentsService.getIncidentsByAgent(testInfo.getUsername())).thenReturn(testIncidents);

		this.mockMvc = MockMvcBuilders.standaloneSetup(incidentsInfoController).build();

	}

	
	/**
     * Tests that a POST request requesting the incidents of
     * a sample agent who already submitted one, comparing the received
     * data with that of the agent's.
     * @throws Exception
     */
	@Test
	public void testIncidentsInfoContentOK() throws Exception {
		String payload = buildPayload("Ejemplo", "pass", "Person");
		MockHttpServletRequestBuilder request = post("/incidentsinfo")
				.content(payload.getBytes())
				.contentType(MediaType.APPLICATION_JSON);

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

	/**
     * Tests that a POST request requesting the incidents of
     * a non-registered agent returns a status code of 'NOT FOUND'.
     * @throws Exception
     */
	@Test
	public void testIncidentsInfoNotFound() throws Exception {
		String payload = buildPayload("notAnAgent", "whatever", "Entity");
		MockHttpServletRequestBuilder request = post("/incidentsinfo").content(payload.getBytes())
				.contentType(MediaType.APPLICATION_JSON);

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

	}

	/**
     * Sends a GET request to the log-in form URL,testing that it
     * receives an 'OK' status.
     * @throws Exception
     */
	@Test
	public void testIncidentsInfoGetForm() throws Exception {

		MockHttpServletRequestBuilder request = get("/agentform");

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

	/**
     * Sends a GET request to the log-in form URL with the parameter
     * 'error' set to true, meaning an error message will be shown and testing that it
     * receives an 'OK' status.
     * @throws Exception
     */
	@Test
	public void testIncidentsInfoGetFormError() throws Exception {

		MockHttpServletRequestBuilder request = get("/agentform").param("error", "true");

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		assertEquals(HttpStatus.OK.value(), response.getStatus());

	}

	/**
     * Sends a POST request to the log-in form URL with the parameter once the user
     * was logged in correctly, testing that it is redirected to his/her list
     * of submitted incidents.
     * @throws Exception
     */
	@Test
	public void testIncidentsInfoPost() throws Exception {

		MockHttpServletRequestBuilder request = post("/agentform").param("username", "Ejemplo").param("password",
				"pass").param("kind", "Person");

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		// redirect to incidents view
		assertEquals(HttpStatus.FOUND.value(), response.getStatus());

	}
	
	/**
     * Sends a GET request to the incident list view without the user being logged in
     * correctly, testing that it is redirected to the log in form.
     * @throws Exception
     */
	@Test
	public void testShowIncidentsInfoInvalid() throws Exception {
		MockHttpServletRequestBuilder request = get("/incidents");
		int status = mockMvc.perform(request)
				.andExpect(redirectedUrl("/agentform"))
				.andReturn()
				.getResponse()
				.getStatus();
		assertEquals(HttpStatus.FOUND.value(), status);
	}	
	
	/**
     * Sends a GET request to the incident list view with the user logged in
     * correctly, testing that he/she is directed to the incident list.
     * @throws Exception
     */
	@Test
	public void testShowIncidentsInfoValid() throws Exception {
        MockHttpSession session = new MockHttpSession();
        AgentInfo agentInfo = new AgentInfo("Son", "prueba", "Person");
        session.setAttribute("agentInfo", agentInfo);
        
		MockHttpServletRequestBuilder request = get("/incidents").session(session);
		int status = mockMvc.perform(request)
				.andExpect(forwardedUrl("incident_list"))
				.andReturn()
				.getResponse()
				.getStatus();
		assertEquals(HttpStatus.OK.value(), status);
	}

	/**
     * Sends a POST request to log in form view with the user not logged in
     * correctly, testing that he/she is redirected to the form again.
     * @throws Exception
     */
	@Test
	public void testIncidentsInfoPostNonExists() throws Exception {
		MockHttpServletRequestBuilder request = post("/agentform").param("username", "badExample")
				.param("password", "fail").param("kind", "Person");

		MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();

		// Redirected to Login
		assertEquals(HttpStatus.FOUND.value(), response.getStatus());

	}
    
    private String buildPayload(String name, String password, String kind) {
		return String.format("{\"username\": \"%s\", \"password\": \"%s\", "
					+ "\"kind\": \"%s\"}", name, password, kind);
    }
    
    /*
     * Test that for a logged user, accesing '/' redirects to their 
     * sent incidents view.
     * @throws Exception
     */
    @Test
	public void testLandingPageWhenLogged() throws Exception {
		MockHttpSession session = new MockHttpSession();
		AgentInfo agentInfo = new AgentInfo("Son", "prueba", "Person");
	    session.setAttribute("agentInfo", agentInfo);
        
        MockHttpServletRequestBuilder request = get("/").session(session);
        int status = mockMvc.perform(request)
        				.andExpect(redirectedUrl("/incidents"))
        				.andReturn()
        				.getResponse()
        				.getStatus();
        
        assertEquals(HttpStatus.FOUND.value(), status);
    }
    
    /*
     * Test that for an unknown user, accesing '/' redirects to the
     * log in page (passing through '/incidents' first).
     * @throws Exception
     */
    @Test
    public void testLandingPageWhenNotLogged() throws Exception {
    		MockHttpSession session = new MockHttpSession();
        session.removeAttribute("agentInfo");
    	
        MockHttpServletRequestBuilder request = get("/").session(session);
        int status = mockMvc.perform(request)
        				.andExpect(redirectedUrl("/incidents"))
        				.andReturn()
        				.getResponse()
        				.getStatus();
        
        assertEquals(HttpStatus.FOUND.value(), status);
        
        MockHttpServletRequestBuilder request2 = get("/incidents").session(session);
        int status2 = mockMvc.perform(request2)
        				.andExpect(redirectedUrl("/agentform"))
        				.andReturn()
        				.getResponse()
        				.getStatus();
        
        assertEquals(HttpStatus.FOUND.value(), status2);
    }
}
