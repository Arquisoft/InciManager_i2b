package com.uniovi.main.kafka;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaTest.class);

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "incidents");

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	@InjectMocks
	private KafkaService kafkaService;

	private KafkaMessageListenerContainer<String, String> container;

	private BlockingQueue<ConsumerRecord<String, String>> records;

	@Before
	public void setUp() throws Exception {
		this.initKafkaTemplate();
		this.setUpConsumer();
		MockitoAnnotations.initMocks(this);
	}

	@After
	public void tearDown() {
		container.stop();
	}

	private void setUpConsumer() throws Exception {
		Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps("sender", "false", embeddedKafka);

		DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<String, String>(
				consumerProperties);

		// set the topic that needs to be consumed
		ContainerProperties containerProperties = new ContainerProperties("incidents");

		container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
		records = new LinkedBlockingQueue<>();
		container.setupMessageListener(new MessageListener<String, String>() {
			@Override
			public void onMessage(ConsumerRecord<String, String> record) {
				LOGGER.debug("test-listener received message='{}'", record.toString());
				records.add(record);
			}
		});

		container.start();
		ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
	}

	private void initKafkaTemplate() {
		Map<String, Object> senderProperties = KafkaTestUtils.senderProps(embeddedKafka.getBrokersAsString());

		ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<String, String>(
				senderProperties);

		kafkaTemplate = new KafkaTemplate<>(producerFactory);
	}

	@Test
	public void testIncidentToString() {

	}

	@Test
	public void testSendToKafka() throws InterruptedException {
		AgentInfo agentInfo = new AgentInfo("paco", "123456", "Person");
		Incident incident = new Incident("Test", new LatLng(50, 20), agentInfo);
		kafkaService.sendToKafka(incident);
		
	    // check that the message was received
	    ConsumerRecord<String, String> received = records.poll(10, TimeUnit.SECONDS);
	    assertEquals(kafkaService.toKafkaMessage(incident), received.value());
	    assertEquals("incidents", received.key());
	}

}