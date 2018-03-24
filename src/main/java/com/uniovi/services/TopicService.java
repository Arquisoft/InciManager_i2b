package com.uniovi.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uniovi.entities.Incident;

@Service
public class TopicService 
{
	
	public List<String> getTopicsOf(Incident incident)
	{
		List<String> aux = new ArrayList<String>();
		if(incident.getLocation()!=null)
			aux.add("geolocated");
		if(hasOperator(incident))
			aux.add("withOperator");
		if(("Sensor".equalsIgnoreCase(incident.getAgent().getKind())))
			aux.add("sensor");
		aux.add("incidents");
		return aux;
	}
	
	private boolean hasOperator(Incident incident)
	{
		for (String key : incident.getProperties().keySet()) 
		{
		    if("operators".equalsIgnoreCase(key))
		    {
		        return true;
		    }
		}
		return false;
	}

}
