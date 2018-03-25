package com.uniovi.entities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uniovi.json.IncidentDeserializer;
import com.uniovi.json.IncidentSerializer;

@JsonDeserialize(using = IncidentDeserializer.class)
@JsonSerialize(using = IncidentSerializer.class)
@Document(collection="incidents")
public class Incident {

    @Id
    private ObjectId id;
	
	private String inciName;
	private LatLng location;
	
	@Field("agent")
	private AgentInfo agent;
	
	private List<String> tags = new ArrayList<String>();
	
	private List<String> moreInfo = new ArrayList<String>();

	private List<String> comments = new ArrayList<String>();
	
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	private IncidentState state;

	public Incident() {}
	
	/**
	 * Minimum initialization of Incident object. None of the parameters can be empty or null
	 * 
	 * @param username - of the user that submitted the incident
	 * @param passw - of the user that submitted the incident
	 * @param name - of the incident, either descriptive or a code
	 * @param location - of the incident 
	 */
	public Incident(String name, LatLng location) {
		if (name.equals("") || location == null)
			throw new IllegalArgumentException("Incident fields cannot be empty");
		
		this.inciName = name;
		this.location = location;
	}
	
	public Incident(String name, LatLng latLng, AgentInfo agent) {
		this(name, latLng);
		this.setAgent(agent);
	}

	public void addMoreInfo(String info) {
		this.moreInfo.add(info);
	}

	public void addProperty(String property, Object value) {
		this.properties.put(property, value);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public void setInciName(String inciName) {
		this.inciName = inciName;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public ObjectId getId() {
		return id;
	}

	public String getInciName() {
		return inciName;
	}

	public LatLng getLocation() {
		return location;
	}

	public List<String> getTags() {
		return tags;
	}
	
	public List<String> getMoreInfo() {
		return moreInfo;
	}

	public IncidentState getState() {
		return state;
	}

	public void setState(IncidentState state) {
		this.state = state;
	}

	public AgentInfo getAgent() {
		return agent;
	}

	public void setAgent(AgentInfo agent) {
		this.agent = agent;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public Incident addTag(String tag) {
		this.tags.add(tag);
		return this;
	}

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}
	
	public Incident addComment(String comment) {
		this.comments.add(comment);
		return this;
	}
	
	public void assignOperator(Operator randomOperator) {
		this.properties.put("operator", randomOperator.getEmail());
	}
	
	public String getOperator() {
		return (String) this.properties.get("operator");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inciName == null) ? 0 : inciName.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Incident other = (Incident) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return this.inciName.equals(other.inciName);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Incident [id=").append(id).append(", inciName=").append(inciName)
				.append(", location=").append(location).append(", tags=").append(tags)
				.append(", moreInfo=").append(moreInfo).append(", properties=")
				.append(properties).append("]");
		return builder.toString();
	}

}
