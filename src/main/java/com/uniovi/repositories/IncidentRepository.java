package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.uniovi.entities.Incident;

@Repository
public interface IncidentRepository extends MongoRepository<Incident, Long>{
	
	List<Incident> findAllByUsername (String username);
	List<Incident> findAllByUsernameAndPassword (String username);

}
