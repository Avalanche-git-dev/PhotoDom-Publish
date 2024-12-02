package com.app.userservice.configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;


//@Configuration
//@EnableWebSecurity
//public class SecurityConfig  {
	
//	@Autowired
//	JwtAuthenticationFilter JwtAuthenticationFilter;
	
	
	
//	   @Bean
//	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	        http
//	            .csrf(csrf -> csrf.disable()) // Disabilita CSRF
//	            .authorizeHttpRequests(authz -> authz
//	                //.requestMatchers("/api/users/login", "/api/users/register").permitAll() // Permetti l'accesso a login e register
//	                .anyRequest().authenticated() // Richiedi autenticazione per tutte le altre richieste
//	            )
//	            .httpBasic(Customizer.withDefaults()); // Abilita Basic Authentication
//
//	        return http.build();
//	    }
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Bean
//	@Order(1)
//	public SecurityFilterChain basicAuthFilterChain(HttpSecurity http) throws Exception {
//	    http
//	        .securityMatcher("/keycloak/**") // Filtra solo le richieste a "/users/**"
//	        .authorizeHttpRequests(authorize -> authorize
//	            .anyRequest().authenticated()
//	        )
//	        .httpBasic(Customizer.withDefaults()) // Abilita HTTP Basic
//	        .csrf(csrf -> csrf.disable()); // Disabilita CSRF per semplicità
//
//	    return http.build();
//	}
//
//	
//	
//	@Bean
//	@Order(2)
//	public SecurityFilterChain jwtAuthFilterChain(HttpSecurity http) throws Exception {
//	    http
//	        .securityMatcher("/api/**") // Filtra solo le richieste a "/api/**"
//	        .authorizeHttpRequests(authorize -> authorize
//	            .anyRequest().authenticated()
//	        )
//	        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // Abilita JWT
//	        .csrf(csrf -> csrf.disable()); // Disabilita CSRF per semplicità
//
//	    return http.build();
//	}

	
//    @Bean
//    @Order(1) // Priorità più alta per HTTP Basic
//    public SecurityFilterChain basicAuthFilterChain(HttpSecurity http) throws Exception {
//        http
//            .securityMatcher("/keycloak/**") // Solo per "/keycloak/**"
//            .authorizeHttpRequests(auth -> auth
//                .anyRequest().authenticated() // Richiede autenticazione
//            )
//            .httpBasic(Customizer.withDefaults()) // Abilita HTTP Basic
//            .csrf(csrf -> csrf.disable()); // Disabilita CSRF per semplicità
//
//        return http.build();
//    }
//
//    @Bean
//    @Order(2) // Priorità più bassa per JWT
//    public SecurityFilterChain jwtAuthFilterChain(HttpSecurity http) throws Exception {
//        http
//            .securityMatcher("/api/**") // Solo per "/api/**"
//            .authorizeHttpRequests(auth -> auth
//                .anyRequest().authenticated() // Richiede autenticazione
//            )
//            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // Abilita JWT
//            .csrf(csrf -> csrf.disable()); // Disabilita CSRF per semplicità
//
//        return http.build();
//    }
//    
    
    
  
	
	
	
	
	
	
	
//	
//	
//	
//
//	    @Bean
//	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	        http
//	            .csrf(csrf -> csrf.disable()) // Disabilita CSRF per semplicità
//
//	            // Configura autorizzazioni
//	            .authorizeHttpRequests(auth -> auth
//	                .requestMatchers("/api/keycloak/**").permitAll() // Endpoint Keycloak aperti temporaneamente
//	                .requestMatchers("/api/admins/**").hasAuthority("ROLE_ADMIN") // Endpoint Admin protetti
//	                .requestMatchers("/api/users/**").authenticated() // Endpoint Users protetti
//	                .anyRequest().authenticated() // Altri endpoint protetti
//	            )
//
//	            // Configura HTTP Basic per Keycloak
//	            .httpBasic(Customizer.withDefaults())
//
//	            // Configura OAuth2 Resource Server per JWT
//	            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
//
//	            // Configura sessioni stateless
//	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//	        return http.build();
//	    }
//	
//	
//	
//	
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		
	
	
	
	
	
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

//     @Bean
//     PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}








@Configuration
public class SecurityConfig {

    // Autenticazione per Basic Auth
    @Bean
    @Order(1)
     SecurityFilterChain basicAuthFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/keycloak/**") // Filtra solo "/keycloak/**"
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated()) // Richiede autenticazione
            .httpBasic(Customizer.withDefaults()) // Abilita HTTP Basic
            .csrf(csrf -> csrf.disable()); // Disabilita CSRF per semplicità
        return http.build();
    }

    // Autenticazione per JWT
    @Bean
    @Order(2)
     SecurityFilterChain jwtAuthFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**") // Filtra "/api/**"
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/admins/**").hasRole("ADMIN") // Solo gli ADMIN possono accedere
                .anyRequest().authenticated() // Tutti gli altri richiedono autenticazione
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            .csrf(csrf -> csrf.disable()); // Disabilita CSRF per semplicità
        return http.build();
    }

//    private JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Aggiunge "ROLE_" ai ruoli
//        grantedAuthoritiesConverter.setAuthoritiesClaimName("realm_access.roles"); // Specifica il percorso dei ruoli nel JWT
//
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//        return converter;
//    }
    
    
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Estrai il ruolo direttamente dal campo "role" del token JWT
            String role = jwt.getClaimAsString("role"); // Campo "role" nel token JWT
            if (role != null) {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            }
            return Collections.emptyList(); // Nessun ruolo trovato
        });

        return converter;
    }


}

