package example.keycloak.customprovider.provider;


import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import example.keycloak.customprovider.service.UserService;

public class CustomUserStorageProviderFactory implements UserStorageProviderFactory<CustomUserStorageProvider> {
    @Autowired
    private ApplicationContext applicationContext;

    public CustomUserStorageProviderFactory() {
    }



	@Override
    public CustomUserStorageProvider create(KeycloakSession session, ComponentModel model) {
        UserService userService = applicationContext.getBean(UserService.class);
        return new CustomUserStorageProvider(session, model, userService);
    }

    @Override
    public String getId() {
        return "custom-user-storage-provider";
    }



	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}



	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
    
    
    
}
