package com.uniovi.main.cucumber.steps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.uniovi.controllers.IncidentController;
import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.Operator;
import com.uniovi.kafka.KafkaService;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.services.OperatorsService;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SpringBootTest( classes=InciManagerI2bApplication.class )
public class ReportIncidentSteps {
	
    @Mock
    private AgentsService agentsService;
    
    @Mock
    private IncidentsService incidentsService;

    @Mock
    public KafkaService kafkaService;

    @Mock
    public OperatorsService operatorsService;

    @InjectMocks
    private IncidentController incidentController;

    private MockMvc mockMvc;
    private MockHttpServletResponse result;
    private List<Incident> incidents;
    
	@Given("^a list of users:$")
    public void a_list_of_sensors(List<AgentInfo> agents) {
		incidents = new ArrayList<Incident>();
		
        MockitoAnnotations.initMocks(this);
        when(operatorsService.randomOperator()).thenReturn(new Operator());
        doAnswer(invocation -> {
        		incidents.add(invocation.getArgument(0));
        		return null;
        }).when(incidentsService).addNewIncident(Mockito.any(Incident.class));
    		
        for (AgentInfo agent : agents) {
    			when(agentsService.existsAgent(agent)).thenReturn(true);
    		}
    		
    		this.mockMvc = MockMvcBuilders.standaloneSetup(incidentController).build();
    }
    
    @When("^the agent logs in with username \"([^\"]*)\" password \"([^\"]*)\" and kind \"([^\"]*)\"$")
    public void the_user_logs_in_with_username_password_and_kind(String username,
    			String password, String kind) throws Exception {
    		mockMvc.perform(get("/agentform"));
    		MockHttpServletRequestBuilder request = post("/agentform").param("username", username)
    				.param("password", password).param("kind", kind);
    		
    		result = mockMvc.perform(request).andReturn().getResponse();
    }

	
    @When("^the agent with username \"([^\"]*)\" password \"([^\"]*)\" and kind \"([^\"]*)\" posts an incident$")
    public void the_agent_with_username_and_password_posts_an_incident(String username,
    			String password, String kind) throws Exception {
    	
//    		//Login
//    		the_user_logs_in_with_username_password_and_kind(username, password, kind);
//    		the_agent_receives_status_code_of(302);
//    		
//    		//Access incidentform
//    		mockMvc.perform(get("/incident/create?method=form"));
//    		the_agent_receives_status_code_of(200);
    	
    		//Send post method
    		String payload = String.format("{\"agent\": {\"username\": \"%s\", \"password\": \"%s\", "
					+ "\"kind\": \"%s\"}, \"inciName\": \"Test\", \"location\": {\"lat\": 50.2, "
					+ "\"lon\": 12.2}, \"tags\": [], \"moreInfo\": [], \"properties\": {}}",
					username, password, kind);
    	
    		MockHttpServletRequestBuilder request = post("/incident/create")
    				.contentType(MediaType.APPLICATION_JSON).content(payload.getBytes());
    		
    		result = mockMvc.perform(request).andReturn().getResponse();
    }
    
    @When("^the sensor with username \"([^\"]*)\" and password \"([^\"]*)\" posts an incident$")
    public void the_sensor_with_username_and_password_posts_an_incident(String username,
    			String password) throws Exception {
    		String payload = String.format("{\"agent\": {\"username\": \"%s\", \"password\": \"%s\", "
					+ "\"kind\": \"Sensor\"}, \"inciName\": \"Test\", \"location\": {\"lat\": 50.2, "
					+ "\"lon\": 12.2}, \"tags\": [], \"moreInfo\": [], \"properties\": {}}",
					username, password);
    	
    		MockHttpServletRequestBuilder request = post("/incident/create")
    				.contentType(MediaType.APPLICATION_JSON).content(payload.getBytes());
    		
    		result = mockMvc.perform(request).andReturn().getResponse();
    }
    
    @Then("^the agent receives status code of (\\d+)$")
	public void the_agent_receives_status_code_of(int status) {
    		assertThat(result.getStatus(), is(status));
    }
    
    @Then("^the agent receives the string \"([^\"]*)\"$")
    public void the_agent_receives_the_string(String message) throws UnsupportedEncodingException {
    		assertTrue(message.equals(result.getContentAsString()));
    }
    
    @Then("^the incident is stored$")
    public void the_incident_is_stored() {
    	System.err.println(incidents.size());
    		assertTrue(incidents.size() == 1);
    }
    
    @Then("^an operator is assigned to the incident")
    public void an_operator_is_assigned_to_the_incident() {
    		Incident incident = incidents.get(0);
    		assertTrue(incident.getProperties().containsKey("operator"));
    }
}
