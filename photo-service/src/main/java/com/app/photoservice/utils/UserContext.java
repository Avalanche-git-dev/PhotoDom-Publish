
package com.app.photoservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class UserContext {

	private static final Logger logger = LoggerFactory.getLogger(UserContext.class);

	public Mono<Long> getCurrentUserId() {
		logger.debug("Recupero dell'utente corrente iniziato.");

		return ReactiveSecurityContextHolder.getContext().map(context -> context.getAuthentication())
				.flatMap(authentication -> {
					if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
						logger.error("Il contesto di sicurezza non contiene un'autenticazione valida.");
						return Mono.error(new IllegalStateException("Utente non autenticato"));
					}

					Jwt jwt = (Jwt) authentication.getPrincipal();
					logger.debug("JWT trovato: {}", jwt);

					String userIdClaim = jwt.getClaimAsString("userId");
					if (userIdClaim == null) {
						logger.error("Il claim 'userId' non è presente nel JWT.");
						return Mono.error(new IllegalStateException("Claim 'userId' non trovato nel token JWT"));
					}

					try {
						Long userId = Long.valueOf(userIdClaim);
						logger.info("Utente corrente recuperato con ID: {}", userId);
						return Mono.just(userId);
					} catch (NumberFormatException e) {
						logger.error("Errore durante la conversione del claim 'userId' a Long: {}", userIdClaim, e);
						return Mono.error(new IllegalStateException("Claim 'userId' non è un valore valido", e));
					}
				});
	}

	public Mono<String> getUserRole() {
		logger.debug("Recupero del ruolo utente iniziato.");

		return ReactiveSecurityContextHolder.getContext().map(context -> context.getAuthentication())
				.flatMap(authentication -> {
					if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
						logger.error("Il contesto di sicurezza non contiene un'autenticazione valida.");
						return Mono.error(new IllegalStateException("Utente non autenticato"));
					}

					Jwt jwt = (Jwt) authentication.getPrincipal();
					logger.debug("JWT trovato: {}", jwt);

					String role = jwt.getClaimAsString("role");
					if (role == null) {
						logger.error("Il claim 'role' non è presente nel JWT.");
						return Mono.error(new IllegalStateException("Claim 'role' non trovato nel token JWT"));
					}

					logger.info("Ruolo utente corrente: {}", role);
					return Mono.just(role);
				});
	}
}