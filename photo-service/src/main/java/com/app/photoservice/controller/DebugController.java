package com.app.photoservice.controller;

//@RestController
//@RequestMapping("/debug")
//public class DebugController {
//
//    @GetMapping("/token")
//    public ResponseEntity<Map<String, Object>> getTokenDetails(Authentication authentication) {
//        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Utente non autenticato"));
//        }
//
//        Jwt jwt = (Jwt) authentication.getPrincipal();
//        return ResponseEntity.ok(jwt.getClaims());
//    }
//}
