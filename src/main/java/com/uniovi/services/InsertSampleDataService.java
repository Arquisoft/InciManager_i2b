package com.uniovi.services;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Incident;

@Service
public class InsertSampleDataService {
	
	
	@Autowired
	private IncidentsService incidentsService;
	
	@PostConstruct
	public void init() {

		
		Incident incident1 = new Incident ("user1", "passwd1", "inci1", "location1");
		Incident incident2 = new Incident ("user2", "passwd2", "inci2", "location2");
		Incident incident3 = new Incident ("user3", "passwd3", "inci3", "location3");
		Incident incident4 = new Incident ("user4", "passwd4", "inci4", "location4");
		Incident incident5 = new Incident ("user5", "passwd5", "inci5", "location5");

		
		incidentsService.addIncident(incident1);
		incidentsService.addIncident(incident2);
		incidentsService.addIncident(incident3);
		incidentsService.addIncident(incident4);
		incidentsService.addIncident(incident5);

	}
	
	@PreDestroy
	public void finalize() {
		
		incidentsService.deleteIncidentByName("inci1");
		incidentsService.deleteIncidentByName("inci2");
		incidentsService.deleteIncidentByName("inci3");
		incidentsService.deleteIncidentByName("inci4");
		incidentsService.deleteIncidentByName("inci5");
		incidentsService.deleteIncidentByName("inci6");
	}

}
