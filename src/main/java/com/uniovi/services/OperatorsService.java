package com.uniovi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Operator;
import com.uniovi.repositories.OperatorsRepository;

@Service
public class OperatorsService {

	@Autowired
	OperatorsRepository operatorsRepository;
	
	public List<Operator> getOperators(){
		return operatorsRepository.findAll();
	}
}
