package com.uniovi.main.kafka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.kafka.KafkaConfig;

@SpringBootTest(classes= {
		KafkaConfig.class
})
@RunWith(SpringJUnit4ClassRunner.class)
public class KafkaConfigTest {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	@Autowired
	private KafkaTemplate<String, String> template;
	
	@Autowired
	private ProducerFactory<String, String> producerFactory;
	
	@Autowired
	private Map<String, Object> producerConfigs;
	
	@SuppressWarnings("unchecked")
	@Test
	public void validConfigTest() {
		assertNotNull(template);
		assertNotNull(producerFactory);
		assertNotNull(producerConfigs);
		
		
		Map<String, Object> config = (Map<String, Object>) producerConfigs.get("producerConfigs");	
		assertEquals(bootstrapServers, config.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
		assertEquals(StringSerializer.class, config.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
		assertEquals(StringSerializer.class, config.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
	}
}
