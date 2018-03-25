package com.uniovi.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	@SuppressWarnings("unchecked")
	private List<Function<Incident, Boolean>> relevanceConditions() {
		
		List<Function<Incident, Boolean>> conditions = new ArrayList<Function<Incident, Boolean>>();
		
		Reader reader = null;

			try {
				reader = new FileReader(confPath);
				JsonNode incident = new ObjectMapper().readValue(reader, JsonNode.class);
				/*for (Object obj : incident.fieldNames()) {
					obtainCondition(conditions, incident, (String) obj);
				}*/
				while(incident.fieldNames().hasNext()){
					obtainCondition(conditions, incident, incident.fieldNames().next());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return conditions;
	}

	private void obtainCondition(List<Function<Incident, Boolean>> conditions, JsonNode jsonObject, String key) {
		if ("kind".equals(key))
			conditionsKind(conditions, jsonObject);
		else if ("temperature".equals(key))
			conditionsSensor(conditions, jsonObject, key);
		else if ("pollution".equals(key))
			conditionsSensor(conditions, jsonObject, key);
	}
	
	private void conditionsKind(List<Function<Incident, Boolean>> conditions, JsonNode jsonObject) {
		
		JsonNode kindContents = (JsonNode) jsonObject.get("kind");
		if (kindContents.get("type").equals("values")){
			//JSONArray kinds = (JSONArray) kindContents.get("important");
			if (kindContents.isArray()) {
			    for (final JsonNode objNode : kindContents) {
					Function<Incident, Boolean> function = i -> i.getAgent().getKind().equals(objNode);
					conditions.add(function);
			    }
			}
			/*for (int j = 0; j < kinds.size(); j++) {
				Integer index = j;
				Function<Incident, Boolean> function = i -> i.getAgent().getKind().equals(kinds.get(index));
				conditions.add(function);
			}*/
		}
	}
	
	private void conditionsSensor(List<Function<Incident, Boolean>> conditions, JsonNode jsonObject, String sensorType) {
		JsonNode jsonContents = (JsonNode) jsonObject.get(sensorType);
		if (jsonContents.get("type").equals("range"))
			conditionRange(sensorType, conditions, jsonContents);		
	}
	
	private void conditionRange(String sensorType, List<Function<Incident, Boolean>> conditions, JsonNode jsonObject) {
		Long min =  jsonObject.get("min").asLong();
		Long max =  jsonObject.get("max").asLong();
		
		Function<Incident, Boolean> func = 
				i -> i.getProperties().containsKey(sensorType)
						&& (((Double) i.getProperties().get(sensorType)) < min 
								|| ((Double) i.getProperties().get(sensorType)) > max );
						
		conditions.add(func);
	}
}
