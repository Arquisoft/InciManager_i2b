package com.uniovi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.repositories.IncidentsRepository;
import com.uniovi.util.JasyptEncryptor;

@Service
public class IncidentsService {

	@Autowired
	IncidentsRepository incidentsRepository;

	JasyptEncryptor encryptor = new JasyptEncryptor();

	public void addIncident(Incident incident) {
		incidentsRepository.save(incident);
	}

	public void deleteIncidentById(Long id) {
		incidentsRepository.deleteById(id);
	}

	public void deleteIncidentByName(String inciName) {
		Incident i = incidentsRepository.findByInciName(inciName);
		if (i != null)
			incidentsRepository.delete(i);
	}

	public List<Incident> getIncidentsByAgent(AgentInfo agent) {
		return incidentsRepository.findAllByAgent(agent);
	}

	public List<Incident> getIncidents() {
		return incidentsRepository.findAll();
	}

}
