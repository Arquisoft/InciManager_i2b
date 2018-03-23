package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uniovi.entities.Incident;

@Repository("IncidentsRepository")
public interface IncidentsRepository extends CrudRepository<Incident, Long>{
	
	@Query("SELECT i FROM Incident i WHERE i.agent.username = ?1")
	public List<Incident> findAllByAgent(String username);
	
	public List<Incident> findAll();

	public Incident findByInciName (String inciName);

	public void deleteByInciName(String inciName);
}
