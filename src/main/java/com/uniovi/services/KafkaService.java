package com.uniovi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Incident;

@Service
public class KafkaService {
	
	private static final Logger LOG = LoggerFactory.getLogger(KafkaService.class);
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Value("${app.kafka.topic}")
	private String topic;
	
	public void sendToKafka(Incident incident) {
		String message = this.toKafkaMessage(incident);
		LOG.info("seding message='{}' to topic='{}'", message, topic);
		kafkaTemplate.send(topic, message);
	}
	
	private String toKafkaMessage(Incident incident) {
		return incident.toString();
	}
	
}
