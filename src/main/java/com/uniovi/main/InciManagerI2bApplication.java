package com.uniovi.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan({
	"com.uniovi.repositories",
	"com.uniovi.controllers",
	"com.uniovi.entities",
	"com.uniovi.kafka",
	"com.uniovi.json",
	"com.uniovi.services",
	"com.uniovi.util"
})
@EnableMongoRepositories("com.uniovi.repositories")
public class InciManagerI2bApplication {

	public static void main(String[] args) {
		SpringApplication.run(InciManagerI2bApplication.class, args);
	}
}
