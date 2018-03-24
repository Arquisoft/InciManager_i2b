package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.uniovi.entities.Operator;

@Repository
public interface OperatorsRepository extends CrudRepository<Operator, Long>{
	
	List<Operator> findAll();
	
	Operator findByEmail(String email);
}
