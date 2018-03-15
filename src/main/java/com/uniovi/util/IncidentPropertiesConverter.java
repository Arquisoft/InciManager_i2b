package com.uniovi.util;

import java.util.Map;

import javax.persistence.AttributeConverter;

import com.uniovi.json.JSONHelper;

public class IncidentPropertiesConverter implements AttributeConverter<Map<String, Object>, String> {

	private JSONHelper jsonHelper = new JSONHelper();
	
	@Override
	public String convertToDatabaseColumn(Map<String, Object> properties) {
		return jsonHelper.mapToJson(properties);
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String json) {
		return jsonHelper.jsonToMap(json);
	}
	
}
