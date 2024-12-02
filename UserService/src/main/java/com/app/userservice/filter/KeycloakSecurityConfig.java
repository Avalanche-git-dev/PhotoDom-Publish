package com.app.userservice.filter;

//
//@Configuration
////@Order(1)
//@EnableWebSecurity// Priorità più alta
//public class KeycloakSecurityConfig {
//
////	   @Bean
////    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////            .csrf(csrf -> csrf.disable()) // Disabilita CSRF
////            .authorizeHttpRequests(authz -> authz
////                //.requestMatchers("/api/users/login", "/api/users/register").permitAll() // Permetti l'accesso a login e register
////                .anyRequest().authenticated() // Richiedi autenticazione per tutte le altre richieste
////            )
////            .httpBasic(Customizer.withDefaults()); // Abilita Basic Authentication
////
////        return http.build();
////    }
//	
//	
//
//	    @Bean
//	    public SecurityFilterChain keycloakSecurityFilterChain(HttpSecurity http) throws Exception {
//	        http
//	            .securityMatcher(new AntPathRequestMatcher("/api/keycloak/**")) // Applica solo a /api/keycloak/**
//	            .csrf(csrf -> csrf.disable())
//	            .authorizeHttpRequests(auth -> auth
//	                .anyRequest().authenticated() // Richiede autenticazione
//	            )
//	            .httpBasic(Customizer.withDefaults()) // Abilita HTTP Basic
//	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Stateless
//
//	        return http.build();
//	    }
//
//	   
//	   
//	   
//	   
//	   
//	   
//
//}
