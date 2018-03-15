package com.uniovi.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.uniovi.entities.Incident;

public class IncidentJSONModule extends SimpleModule{
	
	private static final long serialVersionUID = -4442230568888669762L;

	public IncidentJSONModule() {
		this.addDeserializer(Incident.class, new IncidentDeserializer());
		this.addSerializer(Incident.class, new IncidentSerializer());
	}
}
