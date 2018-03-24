package com.uniovi.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Incident;
import com.uniovi.entities.IncidentState;
import com.uniovi.repositories.IncidentsRepository;
import com.uniovi.util.IncidentSelector;

@Service
public class IncidentsService {

	@Autowired
	private IncidentsRepository incidentsRepository;
	
	private IncidentSelector incSelector= new IncidentSelector();
	
	public void addIncident(Incident incident) {
		if (incSelector.isRelevant(incident))
			incidentsRepository.save(incident);
	}

	public void deleteIncidentById(ObjectId id) {
		incidentsRepository.deleteById(id);
	}

	public void deleteIncidentByName(String inciName) {
		incidentsRepository.deleteByInciName(inciName);
	}

	public List<Incident> getIncidentsByAgent(String username) {
		return incidentsRepository.findAllByAgent(username);
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

	public void deleteAll() {
		this.incidentsRepository.deleteAll();
	}

}
