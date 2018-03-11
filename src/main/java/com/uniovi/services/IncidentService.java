package com.uniovi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Incident;
import com.uniovi.repositories.IncidentRepository;

@Service
public class IncidentService {
	
	@Autowired
	IncidentRepository incidentRepository;
	
	public List<Incident> getIncidentsByUsername (String username)
	{
		return incidentRepository.findAllByUsername(username);
	}

}
