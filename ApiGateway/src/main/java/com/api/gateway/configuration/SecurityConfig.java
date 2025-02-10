package com.api.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {



	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource())) // Disabilita CSRF
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
	
	
	    @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.addAllowedOrigin("http://localhost:4200"); // Frontend URL
	        configuration.addAllowedOriginPattern("ws://localhost:4200"); // WebSocket 
	        configuration.addAllowedMethod("*"); // Consenti tutti i metodi HTTP (GET, POST, ecc.)
	        configuration.addAllowedHeader("*"); // Consenti tutti gli header
	        configuration.setAllowCredentials(true); // Permetti le credenziali (JWT, Cookie, ecc.)

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration); // Applica la configurazione a tutte le rotte
	        return source;
	    }
	
	
	
	 private final String[] freeResourceUrls = {"/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
	            "/swagger-resources/**", "/api-docs/**", "/aggregate/**", "/webjars/**","/actuator/**", "/actuator/prometheus", "/api/users/register","/ws/**"};

}