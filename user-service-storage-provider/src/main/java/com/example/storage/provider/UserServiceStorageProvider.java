package com.example.storage.provider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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

import com.example.storage.client.UserServiceClient;
import com.example.storage.model.LoginRequest;
import com.example.storage.model.UserDto;

import jakarta.ws.rs.WebApplicationException;
import lombok.extern.slf4j.Slf4j;




@Slf4j
public class UserServiceStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider,
        CredentialInputValidator {

    private final KeycloakSession session;
    private final ComponentModel model;
    private final UserServiceClient client;
    private final Map<String, UserModel> loadedUsers = new ConcurrentHashMap<>();

    public UserServiceStorageProvider(KeycloakSession session, ComponentModel model, UserServiceClient client) {
        this.session = session;
        this.model = model;
        this.client = client;
    }

    @Override
    public void close() {
        log.debug("Closing UserServiceStorageProvider");
    }

    private void addToCache(UserModel user) {
        loadedUsers.putIfAbsent(user.getUsername(), user);
    }

    private UserModel getFromCache(String username) {
        return loadedUsers.get(username);
    }

    private String extractExternalId(String storageId) {
        return StorageId.externalId(storageId);
    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        log.debug("Fetching user by ID: {}", id);

        String externalId = extractExternalId(id);
        UserModel adapter = getFromCache(externalId);
        if (adapter != null) {
            log.debug("Found user with ID '{}' in cache.", id);
            return adapter;
        }

        try {
            UserDto user = client.getUserById(Long.parseLong(externalId));
            adapter = new UserAdapter(session, realm, model, user);
            addToCache(adapter);
            return adapter;
        } catch (WebApplicationException e) {
            log.warn("User with ID '{}' not found in external service.", id);
            return null;
        }
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        log.debug("Fetching user by username: {}", username);

        UserModel adapter = getFromCache(username);
        if (adapter != null) {
            return adapter;
        }

        try {
            UserDto user = client.getUserByUsername(username);
            adapter = new UserAdapter(session, realm, model, user);
            addToCache(adapter);
            return adapter;
        } catch (WebApplicationException e) {
            log.warn("User with username '{}' not found in external service.", username);
            return null;
        }
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        log.debug("Fetching user by email: {}", email);

        return loadedUsers.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElseGet(() -> {
                    try {
                        UserDto user = client.getUserByEmail(email);
                        UserModel adapter = new UserAdapter(session, realm, model, user);
                        addToCache(adapter);
                        return adapter;
                    } catch (WebApplicationException e) {
                        log.warn("User with email '{}' not found in external service.", email);
                        return null;
                    }
                });
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
                UserAdapter adapter = new UserAdapter(session, realm, model, authenticatedUser);
                addToCache(adapter);
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
    public int getUsersCount(RealmModel realm) {
        log.debug("Fetching total user count.");
        return client.getTotalUserCount(); // Chiama un metodo che restituisce il numero totale di utenti
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, String search, Integer firstResult, Integer maxResults) {
        log.debug("searchForUserStream, search={}, first={}, max={}", search, firstResult, maxResults);

        // Ottieni gli utenti paginati dal client
        List<UserDto> users = client.getUsers(search, firstResult, maxResults);
        return toUserModelStream(users, realm);
    }

    @Override
    public Stream<UserModel> searchForUserStream(RealmModel realm, Map<String, String> params, Integer firstResult, Integer maxResults) {
        log.debug("searchForUserStream, params={}, first={}, max={}", params, firstResult, maxResults);

        // Parametri come filtro di ricerca
        String search = params.getOrDefault("search", null);

        // Ottieni gli utenti paginati
        List<UserDto> users = client.getUsers(search, firstResult, maxResults);
        return toUserModelStream(users, realm);
    }

    private Stream<UserModel> toUserModelStream(List<UserDto> users, RealmModel realm) {
        log.debug("Received {} users from provider", users.size());
        return users.stream().map(user -> new UserAdapter(session, realm, model, user));
    }

	@Override
	public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group, Integer firstResult,
			Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

  @Override
  public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String attrName, String attrValue) {
      log.debug("Searching for users by attribute '{}' with value '{}'.", attrName, attrValue);

      return loadedUsers.values().stream()
              .filter(user -> attrValue.equals(user.getFirstAttribute(attrName)));
  }
  
    
    

  
}