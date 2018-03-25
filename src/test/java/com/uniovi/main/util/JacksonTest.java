package com.uniovi.main.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.util.IncidentSelector;

public class JacksonTest 
{
	Incident incident = new Incident("Inci1",new LatLng(45, 90),
			new AgentInfo("teji", "1234", "Person"));
	
	@Test
	public void test() throws IOException
	{
		IncidentSelector selector = new IncidentSelector();
		assertFalse(selector.isRelevant(incident));
	}
}
