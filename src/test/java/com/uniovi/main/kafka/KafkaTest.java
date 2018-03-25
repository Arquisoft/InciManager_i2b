package com.uniovi.main.kafka;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.kafka.KafkaService;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.TopicService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
public class KafkaTest {
	
	@InjectMocks
	private KafkaService kafkaService;
	
	@Mock
	private TopicService topicService;

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;
	
	private List<String> messages;
	private Incident incident;

	@Before
	public void setUp() throws Exception {
		AgentInfo agentInfo = new AgentInfo("paco", "123456", "Person");
		incident = new Incident("Test", new LatLng(50, 20), agentInfo);
		incident.setLocation(null);
		messages = new ArrayList<String>();       
		
		List<String> topics = new ArrayList<String>();
        topics.add("incidents");
		
        MockitoAnnotations.initMocks(this);
        when(kafkaTemplate.send("incidents", kafkaService.toKafkaMessage(incident)))
        		.thenAnswer((answer) -> {
        			messages.add("Incident sent");
        			return null;
        		});
 
        when(topicService.getTopicsOf(incident)).thenReturn(topics);
	}

	@Test
	public void testIncidentToKafkaMessage() {
		AgentInfo agentInfo = new AgentInfo("paco", "123456", "Person");
		Incident incident = new Incident("Test", new LatLng(50, 20), agentInfo);
		assertEquals("{\"agent\":{\"username\":\"paco\",\"password\":\"123456\","
				+ "\"kind\":\"Person\"},\"inciName\":\"Test\",\"location\":{"
				+ "\"lat\":50.0,\"lon\":20.0},\"tags\":[],\"moreInfo\":[],"
				+ "\"properties\":{}}", kafkaService.toKafkaMessage(incident));

		incident.addTag("Fire");
		incident.addProperty("temperature", 50);
		incident.setLocation(null);
		assertEquals("{\"agent\":{\"username\":\"paco\",\"password\":\"123456\","
				+ "\"kind\":\"Person\"},\"inciName\":\"Test\",\"location\":{}"
				+ ",\"tags\":[\"Fire\"],\"moreInfo\":[],"
				+ "\"properties\":{\"temperature\":50}}", 
				kafkaService.toKafkaMessage(incident));
	}

	@Test
	public void testSendToKafka() throws InterruptedException {
		kafkaService.sendToKafka(incident);
		assertEquals(1, messages.size());
		assertEquals("Incident sent", messages.get(0));
	}

}