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
		if(!incident.getLocation().equals(null))
			aux.add("geolocated");
		//the withOperator topics is not implemented
		if(!incident.getAgent().getKind().equals("sensor"))
			aux.add("sensor");
		if(aux.size()==0)
			aux.add("standard");
		return aux;
	}

}
