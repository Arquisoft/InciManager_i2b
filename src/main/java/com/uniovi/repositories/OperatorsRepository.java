package com.uniovi.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.uniovi.entities.Operator;

@Repository
public interface OperatorsRepository extends MongoRepository<Operator, ObjectId> {
	
	List<Operator> findAll();
	
	Operator findByEmail(String email);
}
