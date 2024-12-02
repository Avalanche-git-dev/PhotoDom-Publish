//package com.app.userservice.filter;
//
//
//import java.io.IOException;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JwtDecoder jwtDecoder;
//
//    public JwtAuthenticationFilter(JwtDecoder jwtDecoder) {
//        this.jwtDecoder = jwtDecoder;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String token = resolveToken(request); // Estrai il token dalla richiesta
//
//        if (token != null) {
//            try {
//                // Decodifica il token JWT
//                Jwt jwt = jwtDecoder.decode(token);
//
//                // Crea l'oggetto Authentication basato sul JWT
//                JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
//
//                // Imposta l'autenticazione nel SecurityContextHolder
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } catch (JwtException e) {
//                // Gestione dell'eccezione per token JWT non valido
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
//                return;
//            }
//        }
//
//        // Continua la catena dei filtri
//        filterChain.doFilter(request, response);
//    }
//
//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}
