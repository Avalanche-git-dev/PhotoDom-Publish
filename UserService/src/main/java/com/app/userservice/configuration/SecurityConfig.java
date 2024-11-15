package com.app.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
//	 @Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authz -> authz
//                .requestMatchers("/auth/**").permitAll()
//                .requestMatchers("/actuator/**").permitAll()// Permetti l'accesso libero agli endpoint di autenticazione
//                .requestMatchers("/public/**").permitAll() // Endpoint pubblici
//                .anyRequest().authenticated() // Tutti gli altri endpoint richiedono autenticazione
//            )
//            .csrf(csrf -> csrf.disable()); // Disabilita CSRF per semplicità (solo in fase di sviluppo)
//        
//        return http.build();
//    }
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable()) // Disabilita CSRF in modo aggiornato
	        .authorizeHttpRequests(authz -> authz
	            .anyRequest().permitAll() // Permetti l'accesso a tutti gli endpoint
	        );

	    return http.build();
	}

     @Bean
     PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
