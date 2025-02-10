package com.app.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfiguration {

	@Bean
	OpenAPI getUserServiceConfig() {
		return new OpenAPI().info(new Info().title("User Service API").description(
				"These are the rest API for User Service, User Service in PhotoDom Architecture act also as Confidential storage Provider.<br>"
						+ "In fact, this interface is only demo cause the high level of security protocols layer.<br> "
						+ "User Storage Provider Custom implementation for integrating with Keycloak as Authorization Server.<br> ")
				.version("1.0"))
				.externalDocs(new ExternalDocumentation()
						.url("https://www.keycloak.org/docs/latest/server_development/index.html#_user-storage-spi")
						.description("Click here for the documentation to customize it by yourself. "));

	}

}
