package com.app.photoservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfiguration {

	@Bean
	OpenAPI getUserServiceConfig() {
		return new OpenAPI().info(new Info().title("Photo Service API")
				.description("These are the rest API for Photo Service").version("1.0"));

	}

}
