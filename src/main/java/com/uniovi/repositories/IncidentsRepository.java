package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.uniovi.entities.Incident;

@Repository("IncidentsRepository")
public interface IncidentsRepository extends MongoRepository<Incident, Long>{
	
	List<Incident> findByUsername (String username);
	List<Incident> findAllByUsernameAndPassword (String username, String Password);
	
	Incident findByInciName (String inciName);

}
