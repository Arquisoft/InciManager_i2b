package com.uniovi.repositories;

import com.uniovi.entities.Incident;
import org.springframework.data.repository.CrudRepository;

public interface IncidentRepository extends CrudRepository<Incident, Long>{

}
