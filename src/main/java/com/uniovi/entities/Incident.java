package com.uniovi.entities;
import com.uniovi.util.JasyptEncryptor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Incident {
	
	@Id @GeneratedValue
	private Long id;
	
	private String username, password;
	private String inciName, location;
	private List<String> tags= new ArrayList<String>();
	private List<String> moreInfo= new ArrayList<String>();
	private Map<String, Object> properties = new HashMap<String, Object>();

	public Incident() {}
	
	/**
	 * Minimum initialization of Incident object. None of the parameters can be empty or null
	 * 
	 * @param username - of the user that submitted the incident
	 * @param passw - of the user that submitted the incident
	 * @param name - of the incident, either descriptive or a code
	 * @param location - of the incident 
	 */
	public Incident(String username, String passw, String name, String location)
	{
		if (username=="" || passw=="" ||name=="" || location=="")
			throw new IllegalArgumentException("Incident fields cannot be empty");
		
		this.username = username;
		this.password = encryptPass(passw);
		this.inciName = name;
		this.location = location;
	}
	
	public void addMoreInfo(String property, Object value)
	{
		properties.put(property, value);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = encryptPass(password);
	}

	public void setInciName(String inciName) {
		this.inciName = inciName;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getInciName() {
		return inciName;
	}

	public String getLocation() {
		return location;
	}

	public List<String> getTags() {
		return tags;
	}
	
	public List<String> getMoreInfo() {
		return moreInfo;
	}

	private String encryptPass(String password){
		JasyptEncryptor encryptor = new JasyptEncryptor();
		return encryptor.encryptPassword(password);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Incident [id=").append(id).append(", username=").append(username).append(", password=")
				.append(password).append(", inciName=").append(inciName).append(", location=").append(location)
				.append(", tags=").append(tags).append(", moreInfo=").append(moreInfo).append(", properties=")
				.append(properties).append("]");
		return builder.toString();
	}
	
	
}
