package com.app.userservice.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserDetailsConfig {

    @Bean
    UserDetailsService userDetailsService(@Value("${spring.security.user.name}") String username,
                                                 @Value("${spring.security.user.password}") String password) {
        return new InMemoryUserDetailsManager(
            User.withUsername(username)
                .password(password) // Password hashata
                .roles("Service") // Ruolo assegnato
                .build()
        );
    }

    @Bean
     PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encoder per la password
    }
}
