package com.uniovi.main.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.uniovi.controllers.IncidentController;
import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.LatLng;
import com.uniovi.entities.Operator;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.kafka.KafkaService;
import com.uniovi.services.OperatorsService;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
@RunWith(SpringJUnit4ClassRunner.class)
public class IncidentControllerTest {

    @Mock
    private AgentsService agentsService;

    @Mock
    public IncidentsService incidentsService;

    @Mock
    public KafkaService kafkaService;

    @Mock
    public OperatorsService operatorsService;

    @InjectMocks
    private IncidentController incidentController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(agentsService.existsAgent(new AgentInfo("Son", "prueba", "Person"))).thenReturn(true);
        when(operatorsService.randomOperator()).thenReturn(new Operator());
        this.mockMvc = MockMvcBuilders.standaloneSetup(incidentController).build();
    }

    /**
     * Test that when we send an incident belonging to an agent
     * that does not exist an AgentNotFoundException is thrown.
     * @throws Exception: AgentNotFoundException
     */
    @Test
    public void testAgentNotExists() throws Exception {
		String payload = buildIncidentPayload("NotAnAgent", "prueba", "Test Incident","Person", new LatLng(25, 42),
				"\"test\"", "\"myImage.jpg\"", "\"priority\": 1");

		MockHttpServletRequestBuilder request = post("/incident/create")
				.contentType(MediaType.APPLICATION_JSON).content(payload.getBytes());

		int status = mockMvc.perform(request)
    						.andReturn()
    						.getResponse()
    						.getStatus();

        assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    /**
     * Test that when send an incident belonging to an agent
     * that exists an OK response is received.
     * @throws Exception
     */
    @Test
    public void testAgentInfoCorrect() throws Exception {
    		String payload = buildIncidentPayload("Son", "prueba", "Person", "Test Incident", new LatLng(25, 12),
    				"\"test\"", "\"myImage.jpg\"", "\"priority\": 1");

    		MockHttpServletRequestBuilder request = post("/incident/create")
    				.contentType(MediaType.APPLICATION_JSON).content(payload.getBytes());

    		int status = mockMvc.perform(request)
        						.andReturn()
        						.getResponse()
        						.getStatus();

        assertEquals(HttpStatus.OK.value(), status);
    }

    /**
     * Test that an agent is redirected to the authentication
     * form if it tries to access directly the chat interface
     * to create an incident.
     * @throws Exception
     */
    @Test
    public void testNotLoggedIn() throws Exception {
    		MockHttpServletRequestBuilder request = get("/incident/create");
    		int status = mockMvc.perform(request)
    							.andExpect(redirectedUrl("/agentform"))
    							.andReturn()
    							.getResponse()
    							.getStatus();
        assertEquals(HttpStatus.FOUND.value(), status);
    }

    /**
     * Test that an agent can access the chat interface to create
     * an incident if he has previously logged in correctly.
     * @throws Exception
     */
    @Test
    public void testLoggedIn() throws Exception {
        MockHttpSession session = new MockHttpSession();
        AgentInfo agentInfo = new AgentInfo("Son", "prueba", "Person");
        session.setAttribute("agentInfo", agentInfo);

        MockHttpServletRequestBuilder request = get("/incident/create").session(session);
    	    int status = mockMvc.perform(request)
    						.andExpect(forwardedUrl("chatroom"))
    						.andReturn()
    						.getResponse()
    						.getStatus();

        assertEquals(HttpStatus.OK.value(), status);
    }


    private String buildIncidentPayload(String name, String password, String kind, String inciName,
    				LatLng location, String tags, String moreInfo, String properties) {
		return String.format("{\"agent\": {\"username\": \"%s\", \"password\": \"%s\", "
					+ "\"kind\": \"%s\"}, \"inciName\": \"%s\", \"location\": {\"lat\": %f, "
					+ "\"lon\": %f}, \"tags\": [%s], \"moreInfo\": [%s], \"properties\": {%s}}",
					name, password, kind, inciName, location.latitude, location.longitude,
					tags, moreInfo, properties);
    }

}
