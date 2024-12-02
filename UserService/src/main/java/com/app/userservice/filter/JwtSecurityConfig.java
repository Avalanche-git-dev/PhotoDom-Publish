package com.app.userservice.filter;

//@Configuration
//@Order(2) // Priorità inferiore
//public class JwtSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/admins/**").hasAuthority("ROLE_ADMIN") // Solo admin
//                .requestMatchers("/api/users/**").authenticated() // Accessibile con JWT
//                .anyRequest().authenticated() // Altri endpoint richiedono autenticazione
//            )
//            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // Configura JWT
//            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Stateless
//
//        return http.build();
//    }
//    
//    
//    
//    
//    @Bean
//    public JwtDecoder jwtDecoder() {
//        return JwtDecoders.fromIssuerLocation("http://localhost:8180/realms/PhotoDom");
//    }
//
//    @Bean
//    public JwtAuthenticationConverter jwtAuthenticationConverter() {
//        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
//        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
//            String role = jwt.getClaimAsString("role");
//            return List.of(new SimpleGrantedAuthority("ROLE_" + role));
//        });
//        return converter;
//    }
//
//}

