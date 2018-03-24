package com.uniovi.main.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.uniovi.controllers.IncidentController;
import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.LatLng;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.services.KafkaService;

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

    @InjectMocks
    private IncidentController incidentController;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(agentsService.existsAgent(new AgentInfo("Son", "prueba", "Person"))).thenReturn(true);

        this.mockMvc = MockMvcBuilders.standaloneSetup(incidentController).build();
    }

    @Test
    public void testAgentNotExists() throws Exception {
		String payload = buildPayload("NotAnAgent", "prueba", "Person", "Test Incident", new LatLng(25, 42),
				"\"test\"", "\"myImage.jpg\"", "\"priority\": 1");
		
		MockHttpServletRequestBuilder request = post("/incident/create")
				.contentType(MediaType.APPLICATION_JSON).content(payload.getBytes());
		
		int status = mockMvc.perform(request)
    						.andReturn()
    						.getResponse()
    						.getStatus();

        assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    public void testAgentInfoCorrect() throws Exception {
    		String payload = buildPayload("Son", "prueba", "Person", "Test Incident", new LatLng(25, 12),
    				"\"test\"", "\"myImage.jpg\"", "\"priority\": 1");
    		
        
    		MockHttpServletRequestBuilder request = post("/incident/create")
    				.contentType(MediaType.APPLICATION_JSON).content(payload.getBytes());
    		
    		int status = mockMvc.perform(request)
        						.andReturn()
        						.getResponse()
        						.getStatus();
        
        assertEquals(HttpStatus.OK.value(), status);
    }    
    
    
    private String buildPayload(String name, String password, String kind, String inciName,
    				LatLng location, String tags, String moreInfo, String properties) {
		return String.format("{\"agent\": {\"username\": \"%s\", \"password\": \"%s\", "
					+ "\"kind\": \"%s\"}, \"inciName\": \"%s\", \"location\": {\"lat\": %f, "
					+ "\"lon\": %f}, \"tags\": [%s], \"moreInfo\": [%s], \"properties\": {%s}}",
					name, password, kind, inciName, location.latitude, location.longitude,
					tags, moreInfo, properties);
    }

}
