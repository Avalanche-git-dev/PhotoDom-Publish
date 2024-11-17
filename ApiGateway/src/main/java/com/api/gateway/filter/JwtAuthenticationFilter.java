package com.api.gateway.filter;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.api.gateway.configuration.JwtUtil;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;
//
//@Component
//public class JwtAuthenticationFilter implements WebFilter {
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//
//        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//            return chain.filter(exchange);
//        }
//
//        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return chain.filter(exchange);
//        }
//
//        String token = authHeader.substring(7);
//
//        try {
//            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            if (claims.getExpiration().before(new Date())) {
//                return Mono.error(new RuntimeException("JWT token is expired"));
//            }
//
//            request.mutate().header("username", claims.getSubject()).build();
//            exchange.mutate().request(request).build();
//        } catch (Exception e) {
//            return Mono.error(new RuntimeException("Invalid JWT token"));
//        }
//
//        return chain.filter(exchange);
//    }
//}

//
//
//@Component
//public class JwtAuthenticationFilter implements WebFilter {
//
//    @Value("${jwt.secret}")
//    private String userServiceSecretKey; // Chiave usata per validare il token di UserService
//
//    @Value("${jwt.wrapping-secret}")
//    private String apiGatewaySecretKey; // Chiave usata per firmare il nuovo token
//    
//    
//   
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//
//        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//            return chain.filter(exchange);
//        }
//
//        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return chain.filter(exchange);
//        }
//
//        String token = authHeader.substring(7);
//
//        try {
//            // Verifica del token JWT originale di UserService
//            SecretKey userServiceKey = Keys.hmacShaKeyFor(userServiceSecretKey.getBytes());
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(userServiceKey)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            // Controlla la scadenza del token
//            if (claims.getExpiration().before(new Date())) {
//                return Mono.error(new RuntimeException("JWT token is expired"));
//            }
//
//            // Genera un nuovo token JWT firmato con la chiave segreta di APIGateway
//            SecretKey apiGatewayKey = Keys.hmacShaKeyFor(apiGatewaySecretKey.getBytes());
//            String newToken = Jwts.builder()
//                    .setClaims(claims)
//                    .setSubject(claims.getSubject())
//                    .setIssuedAt(new Date())
//                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 5)) // 5 ore di validità
//                    .signWith(apiGatewayKey)
//                    .compact();
//
//            // Aggiungi il nuovo token JWT all'header della richiesta
//            request.mutate().header("Authorization", "Bearer " + newToken).build();
//            exchange.mutate().request(request).build();
//        } catch (Exception e) {
//            return Mono.error(new RuntimeException("Invalid JWT token"));
//        }
//
//        return chain.filter(exchange);
//    }
//}





@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${jwt.secret}")
    private String secretKey;
    
    @Autowired
    private JwtUtil jwt;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwt.extractAllClaimByUser(token);

            if (claims.getExpiration().before(new Date())) {
                return Mono.error(new RuntimeException("JWT token is expired"));
            }

            // Genera un nuovo token basato su quello vecchio
            String newToken = jwt.generateWrappedToken(token);

            // Aggiungi il nuovo token all'intestazione Authorization per le richieste successive
            request = request.mutate()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + newToken)
                    .build();
            exchange = exchange.mutate().request(request).build();
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Invalid JWT token"));
        }

        return chain.filter(exchange);
    }
}
