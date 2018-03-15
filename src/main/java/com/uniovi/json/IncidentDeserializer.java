package com.uniovi.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;
import com.uniovi.entities.LatLng;

/**
 * Deserializes a JSON object to a new instance of an Incident class.
 * This deserializer can be used in the controllers to automatically
 * create Incident objects from POST requests in a custom format.
 */
public class IncidentDeserializer extends JsonDeserializer<Incident> {

	@SuppressWarnings("unchecked")
	@Override
	public Incident deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
	    ObjectCodec objectCodec = parser.getCodec();
	    JsonNode jsonNode = objectCodec.readTree(parser);
	    
	    Incident incident = new Incident();
	    incident.setAgent(new AgentInfo(jsonNode.get("agent").get("username").asText(), 
	    		jsonNode.get("agent").get("password").asText(), jsonNode.get("agent").get("kind").asText()));
	    incident.setInciName(jsonNode.get("inciName").asText());
	    incident.setLocation(new LatLng(jsonNode.get("location").get("lat").asDouble(),
	    		jsonNode.get("location").get("lon").asDouble()));
	    
	    
	    Iterator<JsonNode> tagsIter = jsonNode.get("tags").elements();
	    while (tagsIter.hasNext()) {
	    		JsonNode tag = tagsIter.next();
	    		incident.getTags().add(tag.asText());
	    }
	    
	    Iterator<JsonNode> infoIter = jsonNode.get("moreInfo").elements();
	    while (infoIter.hasNext()) {
	    		JsonNode info = infoIter.next();
	    		incident.getMoreInfo().add(info.asText());
	    }
	    
	    JsonNode properties = jsonNode.get("properties");
	    ObjectMapper mapper = new ObjectMapper();
	    incident.setProperties(mapper.convertValue(properties, Map.class));
	    
		return incident;
	}
}
