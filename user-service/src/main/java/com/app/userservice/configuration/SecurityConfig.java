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
            .csrf(csrf -> csrf.disable()); 
        return http.build();
    }

    // Autenticazione per JWT
    @Bean
    @Order(2)
     SecurityFilterChain jwtAuthFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**") // Filtra "/api/**"
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/api/users/register").permitAll()
                .requestMatchers("/api/admins/**").hasRole("ADMIN") // Solo gli ADMIN possono accedere
                .anyRequest().authenticated() // Tutti gli altri richiedono autenticazione
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
            .csrf(csrf -> csrf.disable()); 
        return http.build();
    }
    
    // Path per Doc e Status
    @Bean
    @Order(3)
    SecurityFilterChain swaggerFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/swagger-ui.html","/v3/api-docs","/actuator/**") // Filtra solo Swagger e API docs
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Consenti accesso a tutti
            .csrf(csrf -> csrf.disable()); 
        return http.build();
    }


    
    
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Estrae il ruolo direttamente dal campo "role" del token JWT
            String role = jwt.getClaimAsString("role"); // Campo "role" nel token JWT
            if (role != null) {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            }
            return Collections.emptyList(); // Nessun ruolo trovato
        });

        return converter;
    }


}

