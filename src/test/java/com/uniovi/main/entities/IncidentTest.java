package com.uniovi.main.entities;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.Incident;
import com.uniovi.main.InciManagerI2bApplication;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
@RunWith(SpringJUnit4ClassRunner.class)
public class IncidentTest {
	
	@Test
	public void testCorrectInstatiation() {
		Incident inci = new Incident("cArmeEn","2018#","BrokenFountain-10MAR","Plaza Nautico, Gijón");
		assertFalse(inci.getPassword().equals("2018#"));
		assertTrue(inci.getProperties().isEmpty());
		assertTrue(inci.getMoreInfo().isEmpty());
		assertTrue(inci.getTags().isEmpty());
	}
	
	@Test
	public void testEmptyFields() {
		String username = "car";
		String passw = "2018";
		String name = "nameIncident";
		String locat = "gijon";
		
		try {
			new Incident("", passw, name, locat);
			fail("A IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Incident fields cannot be empty"));	
		}
		
		try {
			new Incident(username, "", name, locat);
			fail("A IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Incident fields cannot be empty"));	
		}
		
		try {
			new Incident(username, passw, "", locat);
			fail("A IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Incident fields cannot be empty"));	
		}
		
		try {
			new Incident(username, passw, name, "");
			fail("A IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Incident fields cannot be empty"));	
		}
	}
	
	@Test
	public void testProperties() {
		Incident inci = new Incident("cArmeEn","2018#","BrokenFountain-10MAR","Plaza Nautico, Gijón");
		inci.addProperty("image", "BrokenFountain-10MAR.png");
		inci.addProperty("description", "Leaks at the base");
		
		assertEquals(2, inci.getProperties().size());
		assertEquals("BrokenFountain-10MAR.png", inci.getProperties().get("image"));
		assertEquals("Leaks at the base", inci.getProperties().get("description"));

	}

}
