package com.uniovi.repositories;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;

/**
 * This class is in charge of parsing the master csv file
 * that contains the mapping between the different agent codes
 * and their name. It provides some methods to obtain the kind
 * name from an agent given the kind code, and also to obtain
 * the kind code given the kind name.
 * 
 * @author Alejandro González Hevia
 *
 */
@Repository
public class CSVFileParser implements MasterFileParser {
	
	private String filePath;
	
	public CSVFileParser() {
		this.filePath = "master.csv";
	}
	
	public CSVFileParser(String filePath) {
		this.filePath = filePath;
	}

	public String getKindNameOf(int kind) throws FileNotFoundException, IOException {
		Reader reader = null;
		
        try {
	        	reader = new FileReader(filePath);
	        	Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
	        	for (CSVRecord record : records) {
	        	    int kindCode = Integer.valueOf(record.get(0));
	        	    if (kindCode == kind) return record.get(1);
	        	}
        } finally {
            if (reader != null) { try { reader.close(); } catch (IOException e) { e.printStackTrace(); } }
        }
        
		return null;
	}

	public int getKindCodeOf(String kindName) throws FileNotFoundException, IOException {
		if (kindName == null) return -1;
		
		Reader reader = null;
		
        try {
	        	reader = new FileReader(filePath);
	        	Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
	        	for (CSVRecord record : records) {
	        	    String name = record.get(1);
	        	    if (kindName.equals(name)) return Integer.valueOf(record.get(0));
	        	}
        } finally {
            if (reader != null) { try { reader.close(); } catch (IOException e) { e.printStackTrace(); } }
        }
        
		return -1;
	}

	
	public List<String> getKindNames() throws IOException {
		Reader reader = null;
		List<String> kindNames = new ArrayList<String>();
		
        try {
	        	reader = new FileReader(filePath);
	        	Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
	        	for (CSVRecord record : records) {
	        		kindNames.add(record.get(1));
	        	}
        } finally {
            if (reader != null) { try { reader.close(); } catch (IOException e) { e.printStackTrace(); } }
        }
        
        return kindNames;
	}

}
