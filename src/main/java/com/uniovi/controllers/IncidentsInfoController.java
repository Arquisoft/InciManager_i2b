package com.uniovi.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.util.exception.AgentNotFoundException;

@Controller
public class IncidentsInfoController {

	@Autowired
	private AgentsService agentsService;

	@Autowired
	private IncidentsService incidentsService;

	@RequestMapping(value = "/incidentsinfo", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public String getIncidentsInfoJSON(@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "password", required = true) String password) throws Exception {

		AgentInfo ainfo = agentsService.findByUsername(username);

		if (!agentsService.existsAgent(ainfo)) {
			throw new AgentNotFoundException();
		}

		List<Incident> agentIncidents = incidentsService.getIncidentsByAgent(ainfo);
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(agentIncidents);

		// return agentIncidents;

	}

	@RequestMapping(value = "/agentform", method = RequestMethod.GET)
	public String getIncidentsInfo(Model model, @RequestParam(name = "error", required = false) String error) {

		if (error != null)
			model.addAttribute("authError", true);

		return "authentication_form";

	}

	@RequestMapping(value = "/agentform", method = RequestMethod.POST)
	public String showIncidentsInfo(Model model, @ModelAttribute AgentInfo ainfo) throws Exception {

//		// Quick way to notice a bad login
//		if (!agentsService.existsAgent(ainfo)) {
//			return "redirect: /agentform?error=true";
//		} else {
//			List<Incident> agentIncidents = incidentsService.getIncidentsByAgent(ainfo);
		
		
		List<Incident> agentIncidents = new ArrayList<Incident> ();
		
		Incident incident1 = new Incident("inci1", new LatLng(124, 152), ainfo);
		Incident incident2 = new Incident("inci2", new LatLng(37.5665, 126.9780), ainfo);
		Incident incident4 = new Incident("inci4", new LatLng(100, 200), ainfo);
		
		List<String> tags = new ArrayList<String>();
		tags.add("tag1");
		tags.add("tag2");
		
		incident1.setTags(tags);
		incident2.setTags(tags);
		
		agentIncidents.add(incident1);
		agentIncidents.add(incident2);

		agentIncidents.add(incident4);


			model.addAttribute("incidentsList", agentIncidents);
			return "incidents";
		

	}

}
