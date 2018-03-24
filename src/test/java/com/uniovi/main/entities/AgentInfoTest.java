package com.uniovi.main.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.main.InciManagerI2bApplication;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
@RunWith(SpringJUnit4ClassRunner.class)
public class AgentInfoTest {

	@Test
	public void testInstantiation() {
		AgentInfo agent = new AgentInfo("agentA", "password", "Person");
		assertEquals("agentA", agent.getUsername());
		assertEquals("password", agent.getPassword());
		assertEquals("Person", agent.getKind());
	}
	
	@Test
	public void testEquals() {
		AgentInfo agent = new AgentInfo("agentA", "password", "Person");
		AgentInfo agentCopy = new AgentInfo("agentA", "password", "Person");
		AgentInfo agent2 = new AgentInfo("agentB", "password", "Person");
		AgentInfo agent3 = new AgentInfo();
		
		assertEquals(agent, agentCopy);
		assertNotEquals(agent, agent2);
		assertNotEquals(agent, agent3);
		
		assertEquals(agent.hashCode(), agentCopy.hashCode());
		assertEquals(agent.hashCode(), agent2.hashCode());
		assertEquals(agent.hashCode(), agent3.hashCode());
	}
	
	@Test
	public void testToString() {
		AgentInfo agent = new AgentInfo("agentA", "password", "Person");
		String str = "AgentInfo [id=null, username=agentA, password=password, kind=Person]";
		assertEquals(str, agent.toString());
		
		agent.setKind("Entity");
		assertEquals("AgentInfo [id=null, username=agentA, password=password, kind=Entity]", agent.toString());
		
		agent.setPassword("none");
		assertEquals("AgentInfo [id=null, username=agentA, password=none, kind=Entity]", agent.toString());
		
		agent.setUsername("username");
		assertEquals("AgentInfo [id=null, username=username, password=none, kind=Entity]", agent.toString());
	}
}
