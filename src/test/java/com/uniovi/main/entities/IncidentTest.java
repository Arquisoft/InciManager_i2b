package com.uniovi.main.entities;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.entities.Operator;
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
		assertNull(inci.getOperator());
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
	
	@Test
	public void testComments() {
		Incident inci = new Incident("BrokenFountain-10MAR", new LatLng(34, 87), carmen);
		assertEquals(0, inci.getComments().size());
		inci.addComment("Hiiiii").addComment("agaiiiiin");
		assertTrue(inci.getComments().contains("Hiiiii"));
		assertTrue(inci.getComments().contains("agaiiiiin"));
		assertEquals(2, inci.getComments().size());
		inci.setComments(new ArrayList<String>());
		assertEquals(0, inci.getComments().size());
	}
	
	@Test
	public void testToString() {
		Incident inci = new Incident("InciTest", new LatLng(55, 42), carmen);
		assertEquals("Incident [id=null, inciName=InciTest, location=Location{"
				+ "Latitude='55.0',Longitude='42.0'}, tags=[], moreInfo=[], "
				+ "properties={}]", inci.toString());
		
		List<String> tags = new ArrayList<String>();
		tags.add("Fire");
		inci.setTags(tags);
		assertEquals("Incident [id=null, inciName=InciTest, location=Location{"
				+ "Latitude='55.0',Longitude='42.0'}, tags=[Fire], moreInfo=[], "
				+ "properties={}]", inci.toString());
		
		inci.setInciName("Another name");
		inci.setLocation(new LatLng(12, 45));
		assertEquals("Incident [id=null, inciName=Another name, location=Location{"
				+ "Latitude='12.0',Longitude='45.0'}, tags=[Fire], moreInfo=[], "
				+ "properties={}]", inci.toString());
	}
	
	@Test
	public void testHashCode() {
		Incident inci = new Incident("Fire in Mallorca", new LatLng(55, 42), carmen);
		Incident inci2 = inci;
		assertEquals(inci2.hashCode(), inci.hashCode());
		
		inci2 = new Incident("Fire in Oviedo", new LatLng(55, 42), carmen);
		assertNotEquals(inci2.hashCode(), inci.hashCode());
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testEquals() {
		Incident inci = new Incident("BrokenFountain-10MAR", new LatLng(55, 42), carmen);
		Incident inciClone = inci;
		Incident inci2 = new Incident("Aaaaaaaa", new LatLng(55, 42), carmen);
		assertTrue(inci.equals(inciClone));
		assertFalse(inci.equals(carmen));
		assertFalse(inci2.equals(inci));
	}
	
	@Test
	public void testOperator() {
		Incident inci = new Incident("Panic At The Disco", new LatLng(55, 42), carmen);
		assertNull(inci.getOperator());
		
		inci.assignOperator(new Operator("juana@laloca.com", "juana", false));
		assertNotNull(inci.getOperator());
		assertEquals("juana@laloca.com", inci.getOperator());
	}

}
