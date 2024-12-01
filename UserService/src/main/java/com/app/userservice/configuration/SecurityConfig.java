package com.app.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
//	@Autowired
//	JwtAuthenticationFilter JwtAuthenticationFilter;
	
	
	
	   @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf(csrf -> csrf.disable()) // Disabilita CSRF
	            .authorizeHttpRequests(authz -> authz
	                .requestMatchers("/api/users/login", "/api/users/register").permitAll() // Permetti l'accesso a login e register
	                .anyRequest().authenticated() // Richiedi autenticazione per tutte le altre richieste
	            )
	            .httpBasic(Customizer.withDefaults()); // Abilita Basic Authentication

	        return http.build();
	    }
	
	
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	    http
//	        // Disabilita CSRF
//	        .csrf(csrf -> csrf.disable())
//	        
//	        // Configurazione per gestire le diverse tipologie di autenticazione
//	        .authorizeHttpRequests(authz -> authz
//	            // Permetti accesso solo con Basic Auth a questi endpoint
//	            .requestMatchers("/api/users/login", "/api/users/register").authenticated()
//	            
//	            // Richiedi autenticazione JWT per tutte le altre richieste
//	            .anyRequest().authenticated()
//	        )
//
//	        // Configurazione per Basic Auth
//	        .httpBasic(Customizer.withDefaults())
//
//	        // Configurazione per JWT tramite OAuth2 Resource Server
//	        .oauth2ResourceServer(oauth2 -> oauth2
//	            .jwt(Customizer.withDefaults())
//	        );
//
//	    return http.build();
//	}


	
	
	
	
//	@Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//	

	
//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	    http
//	        .csrf(csrf -> csrf.disable()) // Disabilita CSRF in modo aggiornato
//	        .authorizeHttpRequests(authz -> authz
//	            .anyRequest().permitAll() // Permetti l'accesso a tutti gli endpoint
//	        );
//
//	    return http.build();
//	}
	
//	
//	@Bean
//	 SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	    http
//	        .csrf(csrf -> csrf.disable()) // Disabilita CSRF per semplicità; valuta di abilitarlo in produzione con le dovute precauzioni
//	        .authorizeHttpRequests(authz -> authz
//	            // Permetti l'accesso pubblico agli endpoint di login e registrazione
//	            .requestMatchers("/api/users/login", "/api/users/register").permitAll()
//	            // Permetti l'accesso agli endpoint /api/admin/** solo agli utenti con ruolo ADMIN
//	            .requestMatchers("/api/admins/**").hasAuthority("ADMIN")
//	            // Richiedi autenticazione per tutti gli altri endpoint
//	            .anyRequest().authenticated()
//	        )
//	        // Aggiungi il filtro JWT prima del filtro di autenticazione standard di Spring Security
//	        .addFilterBefore(JwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//	        // Configura la sessione come stateless, dato che usiamo JWT
//	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//	        // Gestisci gli errori di accesso negato
//	        .exceptionHandling(ex -> ex
//	            .authenticationEntryPoint((request, response, authException) -> {
//	                // Restituisce 401 quando l'autenticazione è assente o non valida
//	                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
//	            })
//	            .accessDeniedHandler((request, response, accessDeniedException) -> {
//	                // Restituisce 403 quando l'accesso è negato (es. ruolo non sufficiente)
//	                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
//	            })
//	        );
//
//	    return http.build();
//	}
//

     @Bean
     PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
