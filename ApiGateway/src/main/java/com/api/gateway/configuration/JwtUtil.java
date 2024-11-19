package com.api.gateway.configuration;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
//
//@Component
//public class JwtUtil {
//
//    @Value("${jwt.secret}")
//    private String userServiceSecretKey; // Chiave usata per verificare i token di UserService
//
//    @Value("${jwt.wrapping-secret}")
//    private String apiGatewaySecretKey; // Chiave usata per firmare i nuovi token
//
//    @Value("${jwt.token.expiration}")
//    private long expirationTime; // Durata di validità del token emesso da APIGateway
//    
//    
//    private Key getSigningKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(apiGatewaySecretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//    
//    private Key generateUserKey () {
//   	 byte[] keyBytes = Decoders.BASE64.decode(userServiceSecretKey);
//   	 return Keys.hmacShaKeyFor(keyBytes);
//   }
//
//    public Claims extractAllClaims(String token, Key key) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//    
//    public Claims extractAllClaimByUser(String token) {
//        return extractAllClaims(token, getSigningKey());
//    }
//
////    public boolean isTokenValid(String token) {
////        try {
////            extractAllClaims(token, generateUserKey());
////            return true;
////        } catch (Exception e) {
////            return false;
////        }
////    }
//    
//    
//
//    public String generateWrappedToken(String oldToken) {
//        Claims claims = extractAllClaims(oldToken,generateUserKey());
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(claims.getSubject())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//}


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String userServiceSecretKey; // Chiave usata per verificare i token di UserService

    @Value("${jwt.wrapping-secret}")
    private String apiGatewaySecretKey; // Chiave usata per firmare i nuovi token

    @Value("${jwt.token.expiration}")
    private long expirationTime; // Durata di validità del token emesso da APIGateway

    // Metodo per ottenere la chiave di firma dell'APIGateway
    private Key getApiGatewaySigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(apiGatewaySecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Metodo per ottenere la chiave di firma del servizio utente
    private Key getUserServiceSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(userServiceSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Metodo generale per estrarre le claims da un token usando una chiave specifica
    public Claims extractAllClaims(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Estrai le claims da un token usando la chiave di firma dell'APIGateway
    public Claims extractAllClaimsByApiGateway(String token) {
        return extractAllClaims(token, getApiGatewaySigningKey());
    }

    // Estrai le claims da un token usando la chiave di firma del servizio utente
    public Claims extractAllClaimsByUserService(String token) {
        return extractAllClaims(token, getUserServiceSigningKey());
    }

    // Metodo per generare un nuovo token basato su un token esistente e firmato con la chiave dell'APIGateway
    public String generateWrappedToken(String oldToken) {
        Claims claims = extractAllClaimsByUserService(oldToken); // Estrai le claims usando la chiave del servizio utente
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.getSubject())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getApiGatewaySigningKey(), SignatureAlgorithm.HS256) // Firma con la chiave dell'APIGateway
                .compact();
    }

    // Metodo per verificare la validità di un token con la chiave del servizio utente
    public boolean isUserTokenValid(String token) {
        try {
            extractAllClaimsByUserService(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Metodo per verificare la validità di un token con la chiave dell'APIGateway
    public boolean isApiGatewayTokenValid(String token) {
        try {
            extractAllClaimsByApiGateway(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

