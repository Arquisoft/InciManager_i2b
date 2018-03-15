package com.uniovi.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uniovi.entities.AgentInfo;

@Repository("AgentsRepository")
public interface AgentsRepository extends CrudRepository<AgentInfo, Long> {

	public AgentInfo findByUsername(String username);

}
