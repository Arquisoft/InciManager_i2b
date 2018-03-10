package com.uniovi.entities;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class IncidentTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testCorrectInstatiation() {
		Incident inci = new Incident("cArmeEn","2018#","BrokenFountain-10MAR","Plaza Nautico, Gijón");
		assertFalse(inci.getPassword().equals("2018#"));
		assertTrue(inci.getMoreInfo().isEmpty());
		assertTrue(inci.getTags().isEmpty());
	}
	
	@Test
	public void testEmptyUsername() {
		String username = "car";
		String passw = "2018";
		String name = "nameIncident";
		String locat = "gijon";
		
		exception.expect(IllegalArgumentException.class);
		Incident inci = new Incident("",passw, name, locat);

		exception.expect(IllegalArgumentException.class);
		inci = new Incident(username, "", name, locat);
		
		exception.expect(IllegalArgumentException.class);
		inci = new Incident(username, passw, "", locat);
		
		exception.expect(IllegalArgumentException.class);
		inci = new Incident(username, passw, name, "");
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
