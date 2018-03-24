package com.uniovi.main.kafka;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.kafka.KafkaService;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class KafkaTest {

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "incidents", "geolocated");

	@Autowired
	private KafkaService kafkaService;

	private KafkaTemplate<String, String> kafkaTemplate;
	private static Consumer<String, String> consumer;

	@Before
	public void setUp() throws Exception {
		this.initKafkaTemplate();
		this.setUpConsumer();
	}

	private void initKafkaTemplate() {
		Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafka);
		ProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
		kafkaTemplate = new KafkaTemplate<String, String>(pf, true);
		kafkaTemplate.setDefaultTopic("incidents");
		kafkaService.setKafkaTemplate(kafkaTemplate);
	}

	private void setUpConsumer() throws Exception {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("sampleRawConsumer", "false", embeddedKafka);
		consumerProps.put("auto.offset.reset", "earliest");

		consumer = new KafkaConsumer<>(consumerProps);
		consumer.subscribe(Collections.singletonList("incidents"));
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
		assertEquals("{\"agent\":{\"username\":\"paco\",\"password\":\"123456\","
				+ "\"kind\":\"Person\"},\"inciName\":\"Test\",\"location\":{"
				+ "\"lat\":50.0,\"lon\":20.0},\"tags\":[\"Fire\"],\"moreInfo\":[],"
				+ "\"properties\":{\"temperature\":50}}", 
				kafkaService.toKafkaMessage(incident));
	}

	@Test
	public void testSendToKafka() throws InterruptedException {
		AgentInfo agentInfo = new AgentInfo("paco", "123456", "Person");
		Incident incident = new Incident("Test", new LatLng(50, 20), agentInfo);
		kafkaService.sendToKafka(incident);

		// check that the message was received
		ConsumerRecord<String, String> received = KafkaTestUtils.getSingleRecord(consumer, "incidents");
		assertEquals(kafkaService.toKafkaMessage(incident), received.value());
	}

}