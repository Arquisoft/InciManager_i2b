package com.uniovi.services;

import org.springframework.stereotype.Service;

@Service
public interface IAgentsService {

	public boolean existsAgent(String username, String password);

}
