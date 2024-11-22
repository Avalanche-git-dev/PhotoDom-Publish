package com.photodom.keycloak.provider;

import java.util.Collections;
import java.util.List;

import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.protocol.ProtocolMapper;
import org.keycloak.provider.ProviderConfigProperty;

public class CustomProtocolMapperFactory implements ProtocolMapper {

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
    public ProtocolMapper create(KeycloakSession session) {
        return new CustomProtocolMapper();
    }
    
    //fino a qui me li hai dati tu 

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Scope config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postInit(KeycloakSessionFactory factory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHelpText() {
	    return "Adds custom attributes (userId, role, qualification) to the access token.";
	}


	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
	    return Collections.emptyList();
	}


	@Override
	public String getProtocol() {
	    return "openid-connect";
	}

	public CustomProtocolMapperFactory() {
	}

	
	
}
