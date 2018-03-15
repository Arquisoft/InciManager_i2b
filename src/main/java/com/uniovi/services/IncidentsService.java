package com.uniovi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.IncidentState;
import com.uniovi.repositories.IncidentsRepository;

@Service
public class IncidentsService {

	@Autowired
	private IncidentsRepository incidentsRepository;

	public void addIncident(Incident incident) {
		incidentsRepository.save(incident);
	}

	public void deleteIncidentById(Long id) {
		incidentsRepository.deleteById(id);
	}

	public void deleteIncidentByName(String inciName) {
		incidentsRepository.deleteByInciName(inciName);
	}

	public List<Incident> getIncidentsByAgent(AgentInfo agent) {
		return incidentsRepository.findAllByAgent(agent);
	}	
	
	public Incident getIncidentByName(String name) {
		return incidentsRepository.findByInciName(name);
	}

	public List<Incident> getIncidents() {
		return incidentsRepository.findAll();
	}

	public void addNewIncident(Incident incident) {
		incident.setState(IncidentState.OPEN);
		this.addIncident(incident);
	}

}
