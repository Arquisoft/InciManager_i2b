package com.uniovi.main.cucumber.steps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Operator;
import com.uniovi.kafka.KafkaService;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.manager.IncidentsInfoController;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.services.OperatorsService;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@SpringBootTest( classes=InciManagerI2bApplication.class )
public class LoginSteps {
	
    @Mock
    private AgentsService agentsService;

    @Mock
    public IncidentsService incidentsService;

    @Mock
    public KafkaService kafkaService;

    @Mock
    public OperatorsService operatorsService;

    @InjectMocks
    private IncidentsInfoController incidentController;

    private MockMvc mockMvc;
    private MockHttpServletResponse result;
    
    @Given("^a list of agents:$")
    public void a_list_of_agents(List<AgentInfo> agents) {
        MockitoAnnotations.initMocks(this);
        when(operatorsService.randomOperator()).thenReturn(new Operator());
    		
        for (AgentInfo agent : agents) {
    			when(agentsService.existsAgent(agent)).thenReturn(true);
    		}
    		
    		this.mockMvc = MockMvcBuilders.standaloneSetup(incidentController).build();
    }
    
    @When("^the user accesses \"(.+)\"$")
    public void the_user_accesses(String url) throws Exception {
    		mockMvc.perform(get(url));
    }
    
    @When("^the user logs in with username \"([^\"]*)\" password \"([^\"]*)\" and kind \"([^\"]*)\"$")
    public void the_user_logs_in_with_username_password_and_kind(String username,
    			String password, String kind) throws Exception {
    		MockHttpServletRequestBuilder request = post("/agentform").param("username", username)
    				.param("password", password).param("kind", kind);
    		
    		result = mockMvc.perform(request).andReturn().getResponse();
    }
    
    @Then("^the user receives status code of (\\d+)$")
	public void the_user_receives_status_code_of(int status) {
    		assertThat(result.getStatus(), is(status));
    }
    
    @Then("^the user receives the page \"(.+)\"$")
    public void the_user_receives_the_page(String page) {
    		assertThat(result.getRedirectedUrl(), is(page));
    }
}
