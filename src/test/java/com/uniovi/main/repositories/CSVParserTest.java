package com.uniovi.main.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.uniovi.repositories.CSVFileParser;

public class CSVParserTest {
	
	private CSVFileParser parser;
	
	@Test
	public void getKindNameTest() throws FileNotFoundException, IOException {
		parser = new CSVFileParser("src/test/master_test.csv");
		assertEquals("Person", parser.getKindNameOf(1));
		assertEquals("Entity", parser.getKindNameOf(2));
		assertEquals("Sensor", parser.getKindNameOf(3));
		assertEquals(null, parser.getKindNameOf(0));
		assertEquals(null, parser.getKindNameOf(4));
	}
	
	@Test
	public void getKindCodeTest() throws FileNotFoundException, IOException {
		parser = new CSVFileParser("src/test/master_test.csv");
		assertEquals(1, parser.getKindCodeOf("Person"));
		assertEquals(2, parser.getKindCodeOf("Entity"));
		assertEquals(3, parser.getKindCodeOf("Sensor"));
		assertEquals(-1, parser.getKindCodeOf("Son"));
		assertEquals(-1, parser.getKindCodeOf(null));
	}
	
	@Test
	public void fileNotFoundTest() {
		parser = new CSVFileParser("src/test/not_a_file.csv");
		try {
			parser.getKindNameOf(1);
			fail();
		} catch (FileNotFoundException fnfe) {
			// should throw this
		} catch (IOException e) {
			fail();
		}
		
		try {
			parser.getKindCodeOf("Person");
			fail();
		} catch (FileNotFoundException fnfe) {
			// should throw this
		} catch (IOException e) {
			fail();
		}
	}
	
	@Test
	public void getKindNamesTest() throws IOException {
		parser = new CSVFileParser("src/test/master_test.csv");
		List<String> kindNames = parser.getKindNames();
		assertTrue(kindNames.contains("Person"));
		assertTrue(kindNames.contains("Sensor"));
		assertTrue(kindNames.contains("Entity"));
		assertEquals(3, kindNames.size());
	}

}
