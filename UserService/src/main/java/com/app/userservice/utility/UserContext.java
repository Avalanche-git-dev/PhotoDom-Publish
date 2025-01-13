
package com.app.userservice.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

	private static final Logger logger = LoggerFactory.getLogger(UserContext.class);

	public Long getCurrentUserId() {
		logger.debug("Recupero dell'utente corrente iniziato.");

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
			logger.error("Il contesto di sicurezza non contiene un'autenticazione valida o il principal non è un Jwt.");
			throw new IllegalStateException("Utente non autenticato");
		}

		// Estrai il token JWT
		Jwt jwt = (Jwt) authentication.getPrincipal();
		logger.debug("JWT trovato: {}", jwt);

		// Estrai il claim "userId"
		String userIdClaim = jwt.getClaimAsString("userId");
		if (userIdClaim == null) {
			logger.error("Il claim 'userId' non è presente nel JWT.");
			throw new IllegalStateException("Claim 'userId' non trovato nel token JWT");
		}

		try {
			Long userId = Long.valueOf(userIdClaim);
			logger.info("Utente corrente recuperato con ID: {}", userId);
			return userId;
		} catch (NumberFormatException e) {
			logger.error("Errore durante la conversione del claim 'userId' a Long: {}", userIdClaim, e);
			throw new IllegalStateException("Claim 'userId' non è un valore valido", e);
		}
	}
}
