package com.goorm.liargame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LiargameApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiargameApplication.class, args);
	}

}
