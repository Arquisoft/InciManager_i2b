package com.uniovi.controllers;

import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.Operator;
import com.uniovi.services.AgentsService;
import com.uniovi.services.IncidentsService;
import com.uniovi.services.KafkaService;
import com.uniovi.services.OperatorsService;
import com.uniovi.util.exception.AgentNotFoundException;

@Controller
public class IncidentController {
	
	@Autowired
	private AgentsService agentsService;
	
	@Autowired
	private IncidentsService incidentsService;
	
	@Autowired
	private OperatorsService operatorService;
	
	@Autowired
	private KafkaService kafkaService;
	
	/**
	 * Entry point for a POST request with information about
	 * the incident to be created. If the agent given the
	 * information exists, the incident will be created and
	 * sent to Kafka. If the agent does not exist, an AgentNotFoundException
	 * will be throws.
	 * @param incident Incident to be reported by the agent.
	 * @return String to notify that the incident was sent if
	 * the agent exists. AgentNotFoundException otherwise.
	 * @throws Exception
	 */
	@RequestMapping(value="/incident/create", method=RequestMethod.POST)
	@ResponseBody
	public String createIncident(@RequestBody Incident incident) throws AgentNotFoundException {
		if (!agentsService.existsAgent(incident.getAgent())) {
			throw new AgentNotFoundException();
		}

		agentsService.addAgent(incident.getAgent());
		Operator op = selectRandomOp();
		incident.assignOperator(op);
		incidentsService.addNewIncident(incident);
		kafkaService.sendToKafka(incident);
		return "Incident correctly sent!";
	}
	
	private Operator selectRandomOp() {
		List<Operator> ops = operatorService.getOperators();
		return ops.get((int) Math.random()* ops.size());
	}

	/**
	 * Entry point for a GET request for an agent that wants
	 * to create an incident using a web interface. The agent
	 * must have logged in in order to create the incident.
	 * @param session
	 * @param model
	 * @return Log in page if the agent is not logged in,
	 * a view to create the incident otherwise.
	 * @throws Exception
	 */
	@RequestMapping(value="/incident/create", method=RequestMethod.GET)
	public String createIncident(HttpSession session, Model model) {
		Object info = session.getAttribute("agentInfo");
		if (info == null) {
			return "redirect:/agentform";
		}
		
		AgentInfo agentInfo = (AgentInfo) info;
		model.addAttribute("agentInfo", agentInfo);
		return "chatroom";
	}

}
