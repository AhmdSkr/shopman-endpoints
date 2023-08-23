package com.example.shopman.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig {

	@Bean
	CommandLineRunner hello() {
		return (args) -> {
			System.out.println("Hello, Spring!");
		};
	}
}
