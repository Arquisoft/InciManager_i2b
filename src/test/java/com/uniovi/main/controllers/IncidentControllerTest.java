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
    AgentsService agentsService;
    
    @Mock
    IncidentsService incidentsService;
    
    @Mock
    KafkaService kafkaService;

    @InjectMocks
    IncidentController incidentController;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(agentsService.existsAgent("Son", "prueba")).thenReturn(true);

        this.mockMvc = MockMvcBuilders.standaloneSetup(incidentController).build();
    }

    @Test
    public void testAgentNotExists() throws Exception {
		String payload = buildPayload("NotAnAgent", "prueba", "Test Incident", "Seoul",
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
    		String payload = buildPayload("Son", "prueba", "Test Incident", "Seoul",
    				"\"test\"", "\"myImage.jpg\"", "\"priority\": 1");
        
    		MockHttpServletRequestBuilder request = post("/incident/create")
    				.contentType(MediaType.APPLICATION_JSON).content(payload.getBytes());
    		
    		int status = mockMvc.perform(request)
        						.andReturn()
        						.getResponse()
        						.getStatus();
        
        assertEquals(HttpStatus.OK.value(), status);
    }
    
    private String buildPayload(String name, String password, String inciName, String location,
    							   String tags, String moreInfo, String properties) {
    	
		return String.format("{\"username\": \"%s\", \"password\": \"%s\", "
					+ "\"inciName\": \"%s\", \"location\": \"%s\", \"tags\": [%s], "
					+ "\"moreInfo\": [%s], \"properties\": {%s}}",
					name, password, inciName, location, tags, moreInfo, properties);
    }
}
