package com.app.photoservice.configuration;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


	
//	@Bean
//	ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
//	    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//	    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
//	        List<String> roles = jwt.getClaimAsStringList("realm_access.roles"); // Usa il claim corretto
//	        if (roles != null) {
//	            return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//	        }
//	        return Collections.emptyList();
//	    });
//	    return new ReactiveJwtAuthenticationConverterAdapter(converter);
//	}
//	
//	
//
//	
//	@Bean
//	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//	    http.csrf(csrf -> csrf.disable())
//	        .authorizeExchange(exchange -> exchange
//	            .anyExchange().authenticated()) // Richiedi autenticazione per tutte le richieste
//	        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
//
//	    return http.build();
//	}
//	
	
	
	
	

	@Bean
	SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
	    http.csrf(csrf -> csrf.disable())
	        .authorizeExchange(exchange -> exchange
	        	.pathMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**").permitAll() // Consenti l'accesso a Swagger
	            .anyExchange().authenticated() // Autentica tutti gli altri endpoint
	        )
	        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

	    return http.build();
	}

	    @Bean
	    ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
	        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
	        
	        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
	            // Estrae il ruolo dal claim "role"
	            String role = jwt.getClaimAsString("role");
	            if (role != null) {
	                // Converte il ruolo in un SimpleGrantedAuthority con prefisso "ROLE_"
	                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
	            }
	            return Collections.emptyList(); // Nessun ruolo trovato
	        });

	        // Adatta il converter per Reactive Security
	        return new ReactiveJwtAuthenticationConverterAdapter(converter);
	    }
	
	
	
	

	
	
	
}