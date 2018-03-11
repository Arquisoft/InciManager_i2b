package com.uniovi.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({
	"com.uniovi.repositories",
	"com.uniovi.controllers",
	"com.uniovi.entities",
	"com.uniovi.services",
	"com.uniovi.util"
})
@EnableJpaRepositories("com.uniovi.repositories")
@EntityScan ("com.uniovi.entities")
public class InciManagerI2bApplication {

	public static void main(String[] args) {
		SpringApplication.run(InciManagerI2bApplication.class, args);
	}
}
