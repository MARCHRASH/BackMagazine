package com.magazine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MagazineApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(MagazineApplication.class, args);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry)
	{
		registry.addMapping("/**").allowedOrigins("http://localhost:4200");
	}
}
