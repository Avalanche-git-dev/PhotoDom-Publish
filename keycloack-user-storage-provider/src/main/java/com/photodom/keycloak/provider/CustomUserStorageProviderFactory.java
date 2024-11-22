package com.photodom.keycloak.provider;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.photodom.keycloak.service.UserService;

public class CustomUserStorageProviderFactory implements UserStorageProviderFactory<CustomUserStorageProvider> {


	@Autowired
	private AnnotationConfigApplicationContext context; //Posso fare in modo che il context
	
	
	    @Override
	    public CustomUserStorageProvider create(KeycloakSession session, ComponentModel model) {
	        UserService userService = context.getBean(UserService.class);
	        return new CustomUserStorageProvider(session, userService, model);
	    }
    

    public CustomUserStorageProviderFactory() {
		}



	@Override
    public String getId() {
        return "custom-user-storage-provider"; // ID univoco del provider
    }
    

	public AnnotationConfigApplicationContext getContext() {
		return context;
	}

	public void setContext(AnnotationConfigApplicationContext context) {
		this.context = context;
	}
    
	
	@Override
	public void close() {
		
	}

    
    
    
}
