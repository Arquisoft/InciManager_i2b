package com.uniovi.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.uniovi.entities.Incident;

@Repository("IncidentsRepository")
public interface IncidentsRepository extends MongoRepository<Incident, ObjectId> {
	
	@Query("{ 'agent.username': ?0 }")
	public List<Incident> findAllByAgent(String username);
	
	public List<Incident> findAll();

	public Incident findByInciName (String inciName);

	@Transactional
	public void deleteByInciName(String inciName);
}
