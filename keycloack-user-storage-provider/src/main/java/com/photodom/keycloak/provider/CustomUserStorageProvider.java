package com.photodom.keycloak.provider;

import java.util.Optional;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

import com.photodom.keycloak.mapping.Admin;
import com.photodom.keycloak.mapping.User;
import com.photodom.keycloak.service.UserService;


public class CustomUserStorageProvider implements UserStorageProvider, UserLookupProvider, CredentialInputValidator {

    private final UserService userService;
    private final KeycloakSession session;
    private final ComponentModel storageProviderModel;

    public CustomUserStorageProvider(KeycloakSession session, UserService userService, ComponentModel storageProviderModel) {
        this.session = session;
        this.userService = userService;
        this.storageProviderModel = storageProviderModel; // Salva il modello
    }


    
    protected boolean validate(RealmModel realm, UserModel userModel, CredentialInput credentialInput) {
        if (!(credentialInput instanceof UserCredentialModel)) {
            return false;
        }

        String username = userModel.getUsername();
        String rawPassword = ((UserCredentialModel) credentialInput).getChallengeResponse();

        try {
            return userService.authenticate(username, rawPassword);
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    private UserModel mapToKeycloakUser(User user, RealmModel realm) {
        // Usa il ComponentModel salvato nel provider
        ComponentModel storageProviderModel = this.storageProviderModel; 

        // Crea il CustomUserAdapter
        CustomUserAdapter userAdapter = new CustomUserAdapter(session, realm, storageProviderModel, user);

        // Imposta attributi personalizzati
        userAdapter.setSingleAttribute("id", String.valueOf(user.getId()));
        userAdapter.setSingleAttribute("role", user.getRole().name());

        if (user instanceof Admin) {
            Admin admin = (Admin) user;
            userAdapter.setSingleAttribute("qualification", admin.getQualification().name());
        }

        return userAdapter;
    }



    @Override
    public void close() {
        // Cleanup se necessario
    }

	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
		return validate(realm, user, credentialInput);
	}

	@Override
	public UserModel getUserById(RealmModel realm, String id) {
		    Optional<User> userOpt = userService.findById(Long.parseLong(id)); // Assumendo che UserService abbia findById
	        return userOpt.map(user -> mapToKeycloakUser(user, realm)).orElse(null);
	}

	@Override
	public UserModel getUserByUsername(RealmModel realm, String username) {
		 	Optional<User> userOpt = userService.findByUsername(username);
	        return userOpt.map(user -> mapToKeycloakUser(user, realm)).orElse(null);
	}

	@Override
	public UserModel getUserByEmail(RealmModel realm, String email) {
		Optional<User> userOpt = userService.findByEmail(email);
        return userOpt.map(user -> mapToKeycloakUser(user, realm)).orElse(null);
	}
}

