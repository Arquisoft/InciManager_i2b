package com.uniovi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Operator;
import com.uniovi.repositories.OperatorsRepository;

@Service
public class OperatorsService {

	@Autowired
	private OperatorsRepository operatorsRepository;
	
	public List<Operator> getOperators(){
		return operatorsRepository.findAll();
	}
	
	public Operator findOperatorByEmail(String email) {
		return operatorsRepository.findByEmail(email);
	}
	
	public void addOperator(Operator op) {
		operatorsRepository.save(op);
	}
	
	public void deleteOperator(Operator op) {
		operatorsRepository.delete(op);
	}

	public Operator randomOperator() {
		List<Operator> ops = getOperators();
		int index = (int) (Math.random()* ops.size());
		return ops.get(index);
	}

	public void deleteAll() {
		for (Operator op : getOperators()) {
			deleteOperator(op);
		}
	}
}
