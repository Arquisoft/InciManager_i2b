package com.uniovi.json;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Component
public class JSONHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(JSONHelper.class);
	
	public <K, V> String mapToJson(Map<String, Object> map) {
		Gson gson = new GsonBuilder().create();
		String jsonString = gson.toJson(map).toString();
		LOG.info("Converting map to json string. From {} to {}", map, jsonString);
		return jsonString;
	}
	
	public <K, V> Map<K, V> jsonToMap(String json) {
		Gson gson = new Gson();
		Map<K, V> map = gson.fromJson(json, new TypeToken<Map<K, V>>(){}.getType());
		LOG.info("Converting json string to map. From {} to {}", json, map);
		return map;
	}

}
