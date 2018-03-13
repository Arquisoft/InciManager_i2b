package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;

@Repository("IncidentsRepository")
public interface IncidentsRepository extends CrudRepository<Incident, Long>{
	
	@Query("SELECT i FROM Incident i WHERE i.agent = ?1")
	List<Incident> findAllByAgent(AgentInfo agent);
	
	List<Incident> findAll();

	Incident findByInciName (String inciName);
}
