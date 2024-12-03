package com.api.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

//	 @Bean
//	    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//	        http
//	            .csrf(csrf -> csrf.disable()) // Disabilita CSRF
//	            .authorizeExchange(exchange -> exchange
//	                .pathMatchers("/auth/**", "/actuator/**", "/public/**").permitAll() // Permetti l'accesso libero a questi endpoint
//	                .anyExchange().authenticated() // Tutti gli altri endpoint richiedono autenticazione
//	            );
//
//	        return http.build();
//	    }

//	
//	@Bean
//	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//	    http
//	        .csrf(csrf -> csrf.disable()) // Disabilita CSRF
//	        .authorizeExchange(exchange -> exchange
//	            .anyExchange().permitAll() // Permetti l'accesso a tutte le richieste
//	        );
//
//	    return http.build();
//	}

	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http.csrf(csrf -> csrf.disable()) // Disabilita CSRF
				.authorizeExchange(exchange -> exchange.pathMatchers(freeResourceUrls)
						.permitAll()
						.anyExchange()
						.authenticated())
						.oauth2ResourceServer(oauth2->oauth2.jwt(Customizer.withDefaults()));// Permetti
																															// l'accesso
																															// a
																															// tutte
																															// le
																															// richieste
				

		return http.build();
	}
	
	
	
	 private final String[] freeResourceUrls = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
	            "/swagger-resources/**", "/api-docs/**", "/aggregate/**", "/webjars/**", "/actuator/prometheus"};

}