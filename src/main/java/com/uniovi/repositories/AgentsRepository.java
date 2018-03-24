package com.uniovi.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uniovi.entities.AgentInfo;

@Repository("AgentsRepository")
public interface AgentsRepository extends CrudRepository<AgentInfo, ObjectId> {

	public AgentInfo findByUsername(String username);

}
