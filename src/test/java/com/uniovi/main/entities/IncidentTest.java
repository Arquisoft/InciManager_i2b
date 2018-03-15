package com.uniovi.main.entities;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.main.InciManagerI2bApplication;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
@RunWith(SpringJUnit4ClassRunner.class)
public class IncidentTest {
	
	private AgentInfo carmen = new AgentInfo("cArmeEn","2018#", "Person"); 
	
	@Test
	public void testCorrectInstatiation() {
		Incident inci = new Incident("BrokenFountain-10MAR", new LatLng(55, 42), carmen);
		assertTrue(inci.getAgent().getUsername().equals("cArmeEn"));
		assertTrue(inci.getProperties().isEmpty());
		assertTrue(inci.getMoreInfo().isEmpty());
		assertTrue(inci.getTags().isEmpty());
	}
	
	@Test
	public void testEmptyFields() {
		String name = "nameIncident";
		LatLng locat = new LatLng(15, 42);
		
		try {
			new Incident("", locat);
			fail("A IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Incident fields cannot be empty"));	
		}
		
		try {
			new Incident(name, null);
			fail("A IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Incident fields cannot be empty"));	
		}
	}
	
	@Test
	public void testProperties() {
		Incident inci = new Incident("BrokenFountain-10MAR", new LatLng(34, 87), carmen);
		inci.addProperty("image", "BrokenFountain-10MAR.png");
		inci.addProperty("description", "Leaks at the base");
		
		assertEquals(2, inci.getProperties().size());
		assertEquals("BrokenFountain-10MAR.png", inci.getProperties().get("image"));
		assertEquals("Leaks at the base", inci.getProperties().get("description"));
	}

}
