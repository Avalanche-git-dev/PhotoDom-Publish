package com.example.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;
import org.keycloak.storage.user.UserQueryProvider;

import com.example.provider.adapter.UserAdapter;
import com.example.provider.client.UserServiceClient;
import com.example.provider.dto.LoginRequest;
import com.example.provider.dto.UserDto;

import jakarta.ws.rs.WebApplicationException;
import lombok.extern.slf4j.Slf4j;




@Slf4j
public class UserServiceStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider,
        CredentialInputValidator {

    private final KeycloakSession session;
    private final ComponentModel model;
    private final UserServiceClient client;
    
    protected Map<String, UserModel> loadedUsers = new HashMap<>();
   

    public UserServiceStorageProvider(KeycloakSession session, ComponentModel model, UserServiceClient client) {
        this.session = session;
        this.model = model;
        this.client = client;
    }

    @Override
    public void close() {
        log.debug("Closing UserServiceStorageProvider");
    }
    

    private String extractExternalId(String storageId) {
        return StorageId.externalId(storageId);
    }
    
   

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return PasswordCredentialModel.TYPE.equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        return supportsCredentialType(credentialType);
    }

    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput credentialInput) {
        log.debug("Validating credentials for user: {}", user.getUsername());

        if (!supportsCredentialType(credentialInput.getType()) || !(credentialInput instanceof UserCredentialModel)) {
            log.warn("Unsupported credential type for user: {}", user.getUsername());
            return false;
        }

        UserCredentialModel passwordCredential = (UserCredentialModel) credentialInput;

        try {
            UserDto authenticatedUser = client.authenticate(
                    new LoginRequest(user.getUsername(), passwordCredential.getChallengeResponse()));

            if (authenticatedUser != null) {
                log.info("Authentication successful for user: {}", user.getUsername());
                return true;
            } else {
                log.warn("Authentication failed for user: {}", user.getUsername());
            }
        } catch (WebApplicationException e) {
            log.warn("Authentication error for user '{}': {}", user.getUsername(), e.getMessage());
        }

        return false;
    }


   
    
    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params, Integer firstResult, Integer maxResults) {
        String search = params != null ? params.get("search") : null;
        log.info("Fetch users from Keycloak with search={}, firstResult={}, maxResults={}", search, firstResult, maxResults);

        try {
            List<UserDto> users = client.getUsers(search, firstResult, maxResults);
            return users.stream()
                    .map(userDto -> new UserAdapter(session, realm, model, userDto));
        } catch (Exception e) {
            log.error("Fetch users from Keycloak failed: {}", e.getMessage(), e);
            return Stream.empty();
        }
    }


    
    @Override
    public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group, Integer firstResult, Integer maxResults) {
        log.info("Fetch group members for group={}, firstResult={}, maxResults={}", group.getName(), firstResult, maxResults);

        try {
            // Implementa una chiamata a user-service per recuperare membri del gruppo, se supportato
            List<UserDto> users = client.getUsers(group.getName(), firstResult, maxResults);
            return users.stream()
                    .map(userDto -> new UserAdapter(session, realm, model, userDto));
        } catch (Exception e) {
            log.error("Fetch group members failed: {}", e.getMessage(), e);
            return Stream.empty();
        }
    }

    
    
    @Override
    public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String attrName, String attrValue) {
        log.info("Fetch users by attribute: {}={}", attrName, attrValue);

        try {
            List<UserDto> users = client.getUsers(attrValue, 0, 50); // Limita i risultati per sicurezza
            return users.stream()
                    .map(userDto -> new UserAdapter(session, realm, model, userDto));
        } catch (Exception e) {
            log.error("Fetch users by attribute failed: {}", e.getMessage(), e);
            return Stream.empty();
        }
    }



	@Override
	public UserModel getUserById(RealmModel realm, String id) {
	    log.info("Fetch user from Keycloak by ID: {}", id);

	    try {
	        UserDto userDto = client.getUserById(Long.valueOf(extractExternalId(id)));
	        return new UserAdapter(session, realm, model, userDto);
	    } catch (Exception e) {
	        log.warn("Fetch user from Keycloak failed by ID: {}", id, e);
	        return null;
	    }
	}


	@Override
	public UserModel getUserByUsername(RealmModel realm, String username) {
	    log.info("Fetch user from Keycloak by username: {}", username);

	    try {
	        UserDto userDto = client.getUserByUsername(username);
	        return new UserAdapter(session, realm, model, userDto);
	    } catch (Exception e) {
	        log.warn("Fetch user from Keycloak failed by username: {}", username, e);
	        return null;
	    }
	}


	@Override
	public UserModel getUserByEmail(RealmModel realm, String email) {
	    log.info("Fetch user from Keycloak by email: {}", email);

	    try {
	        UserDto userDto = client.getUserByEmail(email);
	        return new UserAdapter(session, realm, model, userDto);
	    } catch (Exception e) {
	        log.warn("Fetch user from Keycloak failed by email: {}", email, e);
	        return null;
	    }
	}

}