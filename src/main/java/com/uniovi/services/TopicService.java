package com.uniovi.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uniovi.entities.Incident;

@Service
public class TopicService 
{
	public static List<String> getTopicsOf(Incident incident)
	{
		List<String> aux = new ArrayList<String>();
		if(incident.getLocation()!=null)
			aux.add("geolocated");
		if(hasOperator(incident))
			aux.add("withOperator");
		if(!incident.getAgent().getKind().equals("sensor"))
			aux.add("sensor");
		aux.add("standard");
		return aux;
	}
	
	private static boolean hasOperator(Incident incident)
	{
		for (String key : incident.getProperties().keySet()) 
		{
		    if ("operators".equals(key)) 
		    {
		        return true;
		    }
		}
		return false;
	}

}
