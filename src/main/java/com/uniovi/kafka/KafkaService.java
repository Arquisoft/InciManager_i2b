package com.uniovi.kafka;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniovi.entities.Incident;
import com.uniovi.services.TopicService;

@Service
public class KafkaService {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaService.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private TopicService topicService;

	public void sendToKafka(Incident incident) {
		String message = this.toKafkaMessage(incident);
		List<String> topics = topicService.getTopicsOf(incident);
        for (String topic : topics) {
    			LOG.info("sending message='{}' to topic='{}'", message, topic);
			kafkaTemplate.send(topic, message);
        }
	}

	public String toKafkaMessage(Incident incident) {
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		try {
			result = mapper.writeValueAsString(incident);
		} catch (JsonProcessingException e) {
			LOG.error("Couldn't serialize incident: ", incident.toString());
			LOG.error(e.getMessage());
			result = incident.toString();
		}
		return result;
	}
	
	public void setKafkaTemplate(KafkaTemplate<String, String> template) {
		this.kafkaTemplate = template;
	}

}
