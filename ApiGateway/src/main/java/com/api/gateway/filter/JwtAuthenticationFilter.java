package com.api.gateway.filter;

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



//
//
//@Component
//public class JwtAuthenticationFilter implements WebFilter {
//
//    @Autowired
//    private JwtUtil jwt;
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
//            // Verifica il token con la chiave del servizio utente
//           // Claims claims = jwt.extractAllClaimsByUserService(token);
////if(!jwt.isUserTokenValid(token)) {
////	return Mono.error(new RuntimeException("JWT token is expired"));
////}
////            if (claims.getExpiration().before(new Date())) {
////                return Mono.error(new RuntimeException("JWT token is expired"));
////            }
//
//            // Genera un nuovo token basato su quello vecchio
//            String newToken = jwt.generateWrappedToken(token);
//
//            // Aggiungi il nuovo token all'intestazione Authorization per le richieste successive
//            request = request.mutate()
//                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + newToken)
//                    .build();
//            exchange = exchange.mutate().request(request).build();
//        } catch (ExpiredJwtException e) {
//            return Mono.error(new RuntimeException("JWT token is expired"));
//        }  catch (JwtException e) {
//            return Mono.error(new RuntimeException("Invalid JWT token"));
//        }catch (Exception e) {
//            return Mono.error(new RuntimeException("Invalid JWT signature"));
//        }
//
//        return chain.filter(exchange);
//    }
//}

