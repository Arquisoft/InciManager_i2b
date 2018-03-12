package com.uniovi.util;

import java.util.Map;

import javax.persistence.AttributeConverter;

public class IncidentPropertiesConverter implements AttributeConverter<Map<String, Object>, String> {

	JSONHelper jsonHelper = new JSONHelper();
	
	@Override
	public String convertToDatabaseColumn(Map<String, Object> properties) {
		return jsonHelper.mapToJson(properties);
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String json) {
		return jsonHelper.jsonToMap(json);
	}
	
}
