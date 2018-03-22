package com.uniovi.main.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;
import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.services.KafkaService;
import com.uniovi.services.TopicService;

@SpringBootTest(classes = { InciManagerI2bApplication.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class KafkaServiceTest {

	@Mock
	private KafkaService kafkaService;

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	private Map<String, String> kafkaMessages;
	private static List<Incident> testIncidents;

	@BeforeClass
	public static void prepare() {
		testIncidents = new ArrayList<Incident>();

		AgentInfo testInfo1 = new AgentInfo("agentTest1", "pruebas123", "Person");
		AgentInfo testInfo2 = new AgentInfo("agentTest2", "pruebas123", "Person");
		AgentInfo testInfo3 = new AgentInfo("agentTest3", "pruebas123", "Sensor");

		Incident inciTest1 = new Incident("inciTest1", new LatLng(10, 12), testInfo1);

		Incident inciTest2 = new Incident("inciTest2", new LatLng(52, 42), testInfo2);
		inciTest2.getProperties().put("temperature", 29.3);

		Incident inciTest3 = new Incident("inciTest3", new LatLng(25, 25), testInfo3);

		testIncidents.add(inciTest1);
		testIncidents.add(inciTest2);
		testIncidents.add(inciTest3);

	}

	@Before
	public void setUp() {

		kafkaMessages = new HashMap<String, String>();
		MockitoAnnotations.initMocks(this);

		int i = 1;
		for (Incident incident : testIncidents) {
			for (String topic : TopicService.getTopicsOf(incident)) {
				when(kafkaTemplate.send(topic, kafkaService.toKafkaMessage(incident))).thenAnswer(generateAnswer(i));
			}
			i++;
		}

	}

	@Test
	public void testKafkaSend() throws Exception {

		for (Incident incident : testIncidents) {
			kafkaService.sendToKafka(incident);
		}

		assertEquals(kafkaMessages.get("Incident1"), "Sample message");
		assertEquals(kafkaMessages.get("Incident2"), "Sample message");

	}

	private Answer<Void> generateAnswer(int incident) {
		return new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				kafkaMessages.put("Incident" + incident, "Sample message");
				return null;
			}
		};
	}

}
