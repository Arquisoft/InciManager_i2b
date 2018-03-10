package com.uniovi.entities;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.InciManagerI2bApplication;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
//@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
public class IncidentTest {
	
	@Test
	public void testCorrectInstatiation() {
		Incident inci = new Incident("cArmeEn","2018#","BrokenFountain-10MAR","Plaza Nautico, Gijón");
		assertFalse(inci.getPassword().equals("2018#"));
		assertTrue(inci.getMoreInfo().isEmpty());
		assertTrue(inci.getTags().isEmpty());
	}
	
	@Test
	public void testEmptyFields() {
		String username = "car";
		String passw = "2018";
		String name = "nameIncident";
		String locat = "gijon";
		
		Incident inci;
		try {
			inci = new Incident("", passw, name, locat);
			fail("A IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Incident fields cannot be empty"));	
		}
		
		try {
			inci = new Incident(username, "", name, locat);
			fail("A IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Incident fields cannot be empty"));	
		}
		
		try {
			inci = new Incident(username, passw, "", locat);
			fail("A IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Incident fields cannot be empty"));	
		}
		
		try {
			inci = new Incident(username, passw, name, "");
			fail("A IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertThat(e.getMessage(), is("Incident fields cannot be empty"));	
		}
	}
	
	@Test
	public void testMoreInfo() {
		Incident inci = new Incident("cArmeEn","2018#","BrokenFountain-10MAR","Plaza Nautico, Gijón");
		inci.addMoreInfo("image", "BrokenFountain-10MAR.png");
		inci.addMoreInfo("description", "Leaks at the base");
		
		assertEquals(2, inci.getMoreInfo().size());
		assertEquals("image/BrokenFountain-10MAR.png", inci.getMoreInfo().get(0));
		assertEquals("description/Leaks at the base", inci.getMoreInfo().get(1));

	}

}
