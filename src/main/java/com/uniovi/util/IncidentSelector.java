package com.uniovi.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
				//Set<String> keySet = jsonObject.keySet();
				if (jsonObject.containsKey("kind"))
					conditionsKind(conditions, jsonObject);
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		return conditions;
	}

	private void conditionsKind(List<Function<Incident, Boolean>> conditions, JSONObject jsonObject) {
		JSONObject kindContents = (JSONObject) jsonObject.get("kind");
		JSONArray kinds = (JSONArray) kindContents.get("important");
		for (int j=0; j<kinds.size(); j++) {
			Integer index = j;
			Function<Incident, Boolean> function = i -> i.getAgent().getKind().equals(kinds.get(index));
			conditions.add(function);
		}
	}

}
