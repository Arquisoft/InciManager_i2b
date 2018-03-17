package com.uniovi.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.uniovi.entities.Incident;

public class IncidentSelector {

	private final String confPath="src/main/resources/dataToSave.json";
	
	public boolean isRelevant(Incident incident) {
		for (Function<Incident, Boolean> condition : relevanceConditions()) {
			if (condition.apply(incident))
				return true;
		}
		return false;
	}

	private List<Function<Incident, Boolean>> relevanceConditions() {
		
		List<Function<Incident, Boolean>> conditions = new ArrayList<Function<Incident, Boolean>>();
		
		Reader reader = null;
		JSONParser parser = new JSONParser();

			try {
				reader = new FileReader(confPath);
				JSONObject jsonObject = (JSONObject) parser.parse(reader);
				for (Object obj : jsonObject.keySet()) {
					obtainCondition(conditions, jsonObject, (String) obj);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return conditions;
	}

	private void obtainCondition(List<Function<Incident, Boolean>> conditions, JSONObject jsonObject, String key) {
		if ("kind".equals(key))
			conditionsKind(conditions, jsonObject);
		else if ("temperature".equals(key))
			conditionsSensor(conditions, jsonObject, key);
		else if ("pollution".equals(key))
			conditionsSensor(conditions, jsonObject, key);
	}
	
	private void conditionsKind(List<Function<Incident, Boolean>> conditions, JSONObject jsonObject) {
		
		JSONObject kindContents = (JSONObject) jsonObject.get("kind");
		if (kindContents.get("type").equals("values")){
			JSONArray kinds = (JSONArray) kindContents.get("important");
			for (int j = 0; j < kinds.size(); j++) {
				Integer index = j;
				Function<Incident, Boolean> function = i -> i.getAgent().getKind().equals(kinds.get(index));
				conditions.add(function);
			}
		}
	}
	
	private void conditionsSensor(List<Function<Incident, Boolean>> conditions, JSONObject jsonObject, String sensorType) {
		JSONObject jsonContents = (JSONObject) jsonObject.get(sensorType);
		if (jsonContents.get("type").equals("range"))
			conditionRange(sensorType, conditions, jsonContents);		
	}
	
	private void conditionRange(String sensorType, List<Function<Incident, Boolean>> conditions, JSONObject jsonObject) {
		Long min =  (Long) jsonObject.get("min");
		Long max =  (Long) jsonObject.get("max");
		
		Function<Incident, Boolean> func = 
				i -> i.getProperties().containsKey(sensorType)
						&& (((Double) i.getProperties().get(sensorType)) < min 
								|| ((Double) i.getProperties().get(sensorType)) > max );
						
		conditions.add(func);
	}
}
