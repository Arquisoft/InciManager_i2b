package com.uniovi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniovi.entities.Incident;

public class IncidentSelector {

	private String confPath;
	private List<Function<Incident, Boolean>> conditions;
	
	public IncidentSelector() {
		this("src/main/resources/dataToSave.json");
	}
	
	public IncidentSelector(String confPath) {
		this.confPath = confPath;
		this.conditions = new ArrayList<Function<Incident, Boolean>>();
		this.relevanceConditions(); // init conditions list
	}
	
	public boolean isRelevant(Incident incident) {
		for (Function<Incident, Boolean> condition : this.conditions) {
			if (condition.apply(incident))
				return true;
		}
		return false;
	}

	private List<Function<Incident, Boolean>> relevanceConditions() {
		byte[] jsonData = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			jsonData = Files.readAllBytes(Paths.get(confPath));
			JsonNode root = mapper.readTree(jsonData);
			
			Iterator<Entry<String, JsonNode>> nodes = root.fields();
			while (nodes.hasNext()) {
				Entry<String, JsonNode> currentNode = nodes.next();
				obtainCondition(currentNode.getValue(), currentNode.getKey());
			}
		} catch (IOException e) { e.printStackTrace(); }
		
		return conditions;
	}

	private void obtainCondition(JsonNode jsonObject, String key) {
		if ("kind".equals(key))
			conditionsKind(jsonObject);
		else if ("temperature".equals(key))
			conditionsSensor(jsonObject, key);
		else if ("pollution".equals(key))
			conditionsSensor(jsonObject, key);
	}
	
	private void conditionsKind(JsonNode jsonObject) {
		if (jsonObject.get("type").asText().equals("values")){
			Iterator<JsonNode> kinds = jsonObject.get("important").elements();
			while (kinds.hasNext()) {
				JsonNode currentKind = kinds.next();
				Function<Incident, Boolean> function = i -> i.getAgent().getKind().equals(currentKind.asText());
				conditions.add(function);
			}
		}
	}
	
	private void conditionsSensor(JsonNode jsonObject, String sensorType) {
		if (jsonObject.get("type").asText().equals("range"))
			conditionRange(sensorType, jsonObject);		
	}
	
	private void conditionRange(String sensorType, JsonNode jsonObject) {
		Long min =  jsonObject.get("min").asLong();
		Long max =  jsonObject.get("max").asLong();
		
		Function<Incident, Boolean> func = 
				i -> i.getProperties().containsKey(sensorType)
						&& ((Double.parseDouble(((String) i.getProperties().get(sensorType)))) < min 
								|| (Double.parseDouble(((String) i.getProperties().get(sensorType))) > max ));
						
		this.conditions.add(func);
	}
	
	public String getConfPath() {
		return this.confPath;
	}
}
