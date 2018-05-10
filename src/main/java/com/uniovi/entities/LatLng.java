package com.uniovi.entities;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Utility class that stores a pair latitude/longitude.
 */
public class LatLng {
	
	@Field("lat")
	public double latitude;
	
	@Field("lon")
	public double longitude;
	
	public LatLng() {}
	
	public LatLng(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "Location{Latitude='"+latitude+"',"+
				"Longitude='"+longitude+"'}";
	}
	
}
