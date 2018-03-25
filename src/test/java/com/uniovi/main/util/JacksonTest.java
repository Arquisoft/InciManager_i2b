package com.uniovi.main.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.uniovi.entities.Incident;
import com.uniovi.util.IncidentSelector;

public class JacksonTest 
{
	Incident incident = new Incident();
	
	@Test
	public void test() throws IOException
	{
		IncidentSelector selector = new IncidentSelector();
		assertTrue(selector.isRelevant(incident));
	}
}
