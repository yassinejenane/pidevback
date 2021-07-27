package com.esprit.pi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.esprit.pi.repository.UserRepository;


@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class PiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiApplication.class, args);
		System.out.println("version: " + SpringVersion.getVersion());
		
	}

}
