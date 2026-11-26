package com.pearl.nov25.springproj1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class Springproj1Application {

	public static void main(String[] args) {
		SpringApplication.run(Springproj1Application.class, args);
	}

}
