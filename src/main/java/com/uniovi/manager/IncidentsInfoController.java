package com.uniovi.manager;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.util.exception.AgentNotFoundException;

@Controller
public class IncidentsInfoController {

	@Autowired
	private AgentsService agentsService;

	@Autowired
	private IncidentsService incidentsService;

	/**
	 * Entry point for the POST request that allows an agent
	 * to query its incidents, receiving a JSON response with
	 * every incident it has sent to the system.
	 * @param ainfo Log in information of the agent (username, password
	 * and kind) that wants to query it's incidents.
	 * @return JSON response with every incident that the agent has sent.
	 * @throws Exception
	 */
	@RequestMapping(value = "/incidentsinfo", method = RequestMethod.POST,
			consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String getIncidentsInfoJSON(@RequestBody AgentInfo ainfo ) throws Exception {
		if (!agentsService.existsAgent(ainfo)) {
			throw new AgentNotFoundException();
		}

		List<Incident> agentIncidents = incidentsService.getIncidentsByAgent(ainfo.getUsername());
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(agentIncidents);
	}

	/**
	 * Entry point for the GET request that returns an authentication
	 * view which the user can use to log in the system.
	 * @param model
	 * @param error
	 * @return Authentication view.
	 * @throws IOException
	 */
	@RequestMapping(value = "/agentform", method = RequestMethod.GET)
	public String getIncidentsInfo(Model model, @RequestParam(name = "error", required = false) String error) throws IOException {
		if (error != null)
			model.addAttribute("authError", true);

		model.addAttribute("kindNames", agentsService.getAvailableKindNames());
		return "authentication_form";

	}

	/**
	 * Entry point for the POST requests to log in an agent in the system.
	 * @param session
	 * @param model
	 * @param ainfo Log in information about the agent.
	 * @return Log in view if the authentication was not successful,
	 * a view with all the incidents of the agent otherwise.
	 */
	@RequestMapping(value = "/agentform", method = RequestMethod.POST)
	public String onLogIn(HttpSession session, Model model,
			                        @ModelAttribute AgentInfo ainfo) {
		// Quick way to notice a bad login
		if (!agentsService.existsAgent(ainfo)) {
			return "redirect:/agentform?error=true";
		} else {
			session.setAttribute("agentInfo", ainfo);
			return "redirect:/incidents";
		}
	}

	/**
	 * Entry point for the GET request that allows an agent to query
	 * its incidents using the web interface in a user friendly way.
	 * @param session HttpSession to check if the agent is logged in.
	 * @param model
	 * @return Log in view if the agent is not logged in, incidents view
	 * with all the incidents of the agent otherwise.
	 */
	@RequestMapping(value="/incidents", method=RequestMethod.GET)
	public String showIncidentsInfo(HttpSession session, Model model) {
		Object info = session.getAttribute("agentInfo");
		if (info == null) {
			return "redirect:/agentform";
		}

		AgentInfo ainfo = (AgentInfo) info;
		List<Incident> agentIncidents = incidentsService.getIncidentsByAgent(ainfo.getUsername());

		model.addAttribute("incidentsList", agentIncidents);
		return "incident_list";
	}

	/*
	 * Entry point for the GET request for the home/base url.
	 * Incidents will redirect to the login form if the user 
	 * hasn't authenticated.
	 */
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String landingPage() {
		return "redirect:/incidents";
	}
}
