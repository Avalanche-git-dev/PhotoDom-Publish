export const environment = {
    production: false,
    apiRootUrl: 'http://api-gateway:8080', // ← questo è il nome del servizio del gateway su K8s , da modificare con localhost:8080 se si torna in modalità dev con docker-compose
    keycloakUrl: 'http://keycloak:8180/realms/PhotoDom/protocol/openid-connect' // ← questo è il nome del servizio keycloak su K8s , da modificare con http://localhost:8180/realms/PhotoDom/protocol/openid-connect se si torna in modalità dev con docker-compose
  };
  