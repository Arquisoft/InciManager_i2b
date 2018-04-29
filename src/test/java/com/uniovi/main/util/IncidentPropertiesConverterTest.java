package com.uniovi.main.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.uniovi.main.InciManagerI2bApplication;
import com.uniovi.util.IncidentPropertiesConverter;

@SpringBootTest(classes= {
		InciManagerI2bApplication.class
})
@RunWith(SpringJUnit4ClassRunner.class)
public class IncidentPropertiesConverterTest {

	private IncidentPropertiesConverter converter;
	
	@Before
	public void setUp() {
		this.converter = new IncidentPropertiesConverter();
	}
	
	@Test
	public void testToColumn() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key1", "value1");
		map.put("key2", 42);
		map.put("key3", new Double(5.3));
		map.put("key4", new String[] {"hi", "just", "testing"});
		
		String result = converter.convertToDatabaseColumn(map);
		String expected = "{\"key1\":\"value1\",\"key2\":42,\"key3\":5.3,"
				+ "\"key4\":[\"hi\",\"just\",\"testing\"]}";
		assertEquals(expected, result);
	}
	
	@Test
	public void testToMap() {
		String json = "{\"key1\":\"value1\",\"key2\":42,\"key3\":5.3,"
				+ "\"key4\":[\"hi\",\"just\",\"testing\"], \"key5\":{\"subkey1\": 5.2}}";
		
		Map<String, Object> result = converter.convertToEntityAttribute(json);
		assertEquals(5, result.size());
		assertEquals("value1", result.get("key1"));
		assertEquals(42.0, result.get("key2"));
		assertEquals(5.3, result.get("key3"));
		assertEquals("[hi, just, testing]", result.get("key4").toString());
		assertEquals("{subkey1=5.2}", result.get("key5").toString());
	}
}
