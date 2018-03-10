package com.uniovi.util;

import java.util.Map;

import javax.persistence.AttributeConverter;

import org.springframework.beans.factory.annotation.Autowired;

public class IncidentPropertiesConverter implements AttributeConverter<Map<String, Object>, String> {

	@Autowired
	JsonHelper jsonHelper;
	
	@Override
	public String convertToDatabaseColumn(Map<String, Object> properties) {
		return jsonHelper.mapToJson(properties);
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String json) {
		return jsonHelper.jsonToMap(json);
	}
	
}
