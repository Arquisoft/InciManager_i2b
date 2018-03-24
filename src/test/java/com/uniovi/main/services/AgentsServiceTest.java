package com.uniovi.main.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.AgentsService;

@SpringBootTest(classes = { InciManagerI2bApplication.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class AgentsServiceTest {
	
	@Autowired
	private AgentsService agentsService;
	
	@Spy
	private AgentsService spy;

	private AgentInfo testInfo1;
	private AgentInfo testInfo2;


    @Before
    public void setUp() throws Exception {
        spy = Mockito.spy(new AgentsService());

		testInfo1 = new AgentInfo("agentTest1", "pruebas123", "Person");
		testInfo1.setId(new ObjectId());
		testInfo2 = new AgentInfo("agentTest2", "pruebas123", "Sensor");
		testInfo2.setId(new ObjectId());
        
		HttpEntity<String>goodEntity = this.buildGoodEntity();
        HttpEntity<String> badEntity = this.buildBadEntity();

        Mockito.doReturn(HttpStatus.OK).when(spy).getResponseStatus(null, HttpMethod.POST, goodEntity);
        Mockito.doReturn(HttpStatus.NOT_FOUND).when(spy).getResponseStatus(null, HttpMethod.POST, badEntity);
    }

	@After
	public void clean() {
		if (testInfo1 != null)
			agentsService.deleteAgent(testInfo1);
		if (testInfo2 != null)
			agentsService.deleteAgent(testInfo2);

	}

	private HttpEntity<String> buildGoodEntity() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		
		JSONObject request = new JSONObject();
		request.put("login", testInfo1.getUsername());
        request.put("password", testInfo1.getPassword());
		request.put("kind", testInfo1.getKind());

        return new HttpEntity<String>(request.toString(), headers);
	}

	private HttpEntity<String> buildBadEntity() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		
		JSONObject request = new JSONObject();
		request.put("login", testInfo2.getUsername());
        request.put("password", testInfo2.getPassword());
		request.put("kind", testInfo2.getKind());

        return new HttpEntity<String>(request.toString(), headers);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddNullAgent() {
		agentsService.addAgent(null);
	}
	
	@Test
	public void testExistsAgent() {
		assertTrue(spy.existsAgent(testInfo1));
		assertFalse(spy.existsAgent(testInfo2));
	}

	@Test
	public void testCRUDAgent() {
		agentsService.addAgent(testInfo1);
		assertEquals(testInfo1, agentsService.findByUsername(testInfo1.getUsername()));
		assertEquals(null, agentsService.findByUsername(testInfo2.getUsername()));

		agentsService.addAgent(testInfo2);
		agentsService.deleteAgent(testInfo1);
		assertEquals(testInfo2, agentsService.findByUsername(testInfo2.getUsername()));
		assertEquals(null, agentsService.findByUsername(testInfo1.getUsername()));
	}
	
	@Test
	public void testGetKindNames() throws IOException {
		List<String> kindNames = agentsService.getAvailableKindNames();
		assertEquals(3, kindNames.size());
		assertTrue(kindNames.contains("Person"));
		assertTrue(kindNames.contains("Sensor"));
		assertTrue(kindNames.contains("Entity"));
	}

}
