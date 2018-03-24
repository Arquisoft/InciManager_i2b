package com.uniovi.entities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.uniovi.json.IncidentDeserializer;
import com.uniovi.json.IncidentSerializer;
import com.uniovi.util.IncidentPropertiesConverter;

@JsonDeserialize(using = IncidentDeserializer.class)
@JsonSerialize(using = IncidentSerializer.class)
@Entity
public class Incident {
	
	@Id @GeneratedValue
	private Long id;
	
	private String inciName;
	private LatLng location;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="agent_id")
	private AgentInfo agent;
	
	@ElementCollection(targetClass=String.class)
	private List<String> tags = new ArrayList<String>();
	
	@ElementCollection(targetClass=String.class)
	private List<String> moreInfo = new ArrayList<String>();
	
	@Convert(converter=IncidentPropertiesConverter.class)
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	@Enumerated(EnumType.STRING)
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

	public Long getId() {
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

	public void assignOperator(Operator op) {
		this.properties.put("operator", op);
	}
	
	public Operator getOperator() {
		return (Operator) this.properties.get("operator");
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
		return true;
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
