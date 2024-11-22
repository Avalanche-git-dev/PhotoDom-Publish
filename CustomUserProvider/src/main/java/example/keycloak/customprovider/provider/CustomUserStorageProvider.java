package example.keycloak.customprovider.provider;


import java.util.Optional;

import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

import example.keycloak.customprovider.entity.User;
import example.keycloak.customprovider.service.UserService;

public class CustomUserStorageProvider implements UserStorageProvider, UserLookupProvider {

    private final KeycloakSession session;
    private final ComponentModel model;
    private final UserService userService;

    public CustomUserStorageProvider(KeycloakSession session, ComponentModel model, UserService userService) {
        this.session = session;
        this.model = model;
        this.userService = userService;
    }

    @Override
    public void close() {
        // Cleanup resources if needed
    }

   

    private UserModel mapToKeycloakUser(RealmModel realm, User userEntity) {
        UserModel user = session.users().addUser(realm, userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
       // user.setEnabled(userEntity.isEnabled());
        user.setSingleAttribute("Id",String.valueOf(userEntity.getId()));
        user.setSingleAttribute("Password",String.valueOf(userEntity.getPassword()));
        user.setSingleAttribute("Role",String.valueOf(userEntity.getRole()));
        user.setSingleAttribute("Status",String.valueOf(userEntity.getStatus()));
        
        return user;
    }

	@Override
	public UserModel getUserById(RealmModel realm, String id) {
		 Optional<User> userEntity = userService.findById(Long.valueOf(id));
	        if (userEntity.isPresent()) {
	            return mapToKeycloakUser(realm, userEntity.get());
	        }
	        return null;
	}

	@Override
	public UserModel getUserByUsername(RealmModel realm, String username) {
		 Optional<User> userEntity = userService.findByUsername(username);
	        if (userEntity.isPresent()) {
	            return mapToKeycloakUser(realm, userEntity.get());
	        }
	        return null;
	}

	@Override
	public UserModel getUserByEmail(RealmModel realm, String email) {
		 Optional<User> userEntity = userService.findByEmail(email);
		    if (userEntity.isPresent()) {
		        return mapToKeycloakUser(realm, userEntity.get());
		    }
		    return null;
	}

	public KeycloakSession getSession() {
		return session;
	}

	public ComponentModel getModel() {
		return model;
	}

	public UserService getUserService() {
		return userService;
	}
	
	
}
