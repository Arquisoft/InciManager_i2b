package com.uniovi.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uniovi.entities.AgentInfo;
import com.uniovi.entities.Incident;

@Repository("IncidentsRepository")
public interface IncidentsRepository extends CrudRepository<Incident, Long>{
	
	@Query("SELECT i FROM Incident i WHERE i.agent = ?1")
	public List<Incident> findAllByAgent(AgentInfo agent);
	
	public List<Incident> findAll();

	public Incident findByInciName (String inciName);

	@Transactional
	public void deleteByInciName(String inciName);
}
