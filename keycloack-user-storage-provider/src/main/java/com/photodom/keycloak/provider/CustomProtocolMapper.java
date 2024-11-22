package com.photodom.keycloak.provider;

import java.util.Collections;
import java.util.List;

import org.keycloak.models.ClientSessionContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.oidc.mappers.AbstractOIDCProtocolMapper;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.representations.AccessToken;

public class CustomProtocolMapper extends AbstractOIDCProtocolMapper {

    @Override
    public AccessToken transformAccessToken(AccessToken token, ProtocolMapperModel mappingModel,
                                            KeycloakSession session, UserSessionModel userSession, ClientSessionContext clientSessionCtx) {

        UserModel user = userSession.getUser();
        token.getOtherClaims().put("userId", user.getFirstAttribute("id"));
        token.getOtherClaims().put("role", user.getFirstAttribute("role"));

        if (user.getFirstAttribute("qualification") != null) {
            token.getOtherClaims().put("qualification", user.getFirstAttribute("qualification"));
        }

        return token;
    }

    @Override
    public String getId() {
        return "custom-protocol-mapper";
    }

    @Override
    public String getDisplayCategory() {
        return "Token Mappers";
    }

    @Override
    public String getDisplayType() {
        return "Custom Protocol Mapper";
    }

    
    @Override
    public String getHelpText() {
        return "Adds custom attributes (userId, role, qualification) to the token.";
    }


    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.emptyList();
    }

}
