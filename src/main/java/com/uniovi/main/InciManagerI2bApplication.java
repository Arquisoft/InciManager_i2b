package com.uniovi.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
	"com.uniovi.repositories",
	"com.uniovi.controllers",
	"com.uniovi.entities",
	"com.uniovi.services",
	"com.uniovi.util"
})
public class InciManagerI2bApplication {

	public static void main(String[] args) {
		SpringApplication.run(InciManagerI2bApplication.class, args);
	}
}
