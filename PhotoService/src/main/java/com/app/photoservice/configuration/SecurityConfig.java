package com.app.photoservice.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable()) // Disabilita la protezione CSRF
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll() // Consenti l'accesso a tutti gli endpoint
            )
             // Configura JWT se necessario per una sicurezza futura
            .httpBasic(httpBasic -> httpBasic.disable()) // Disabilita l'autenticazione HTTP Basic
            .formLogin(formLogin -> formLogin.disable()); // Disabilita il form di login

        return http.build();
    }
}