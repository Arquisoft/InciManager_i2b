package com.uniovi.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Incident {
	
	@Id
	private Long id;
	
	private String username, password;
	private String inciName, location;
	private List<String> tags= new ArrayList<String>();
	private List<String> moreInfo= new ArrayList<String>();

	
	public Incident() {}
	
	public void addMoreInfo(String property, String value)
	{
		moreInfo.add(property +"/"+value);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Incident [username=").append(username).append(", password=").append(password)
				.append(", inciName=").append(inciName).append(", location=").append(location).append(", tags=")
				.append(tags).append(", moreInfo=").append(moreInfo).append("]");
		return builder.toString();
	}
}
