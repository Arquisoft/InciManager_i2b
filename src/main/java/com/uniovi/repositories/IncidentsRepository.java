package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uniovi.entities.Incident;

@Repository("IncidentsRepository")
public interface IncidentsRepository extends CrudRepository<Incident, Long>{
	
	List<Incident> findByUsername (String username);
	List<Incident> findAllByUsernameAndPassword(String username, String encryptPassword);
	List<Incident> findAll();

	Incident findByInciName (String inciName);
}
