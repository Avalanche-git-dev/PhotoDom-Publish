package com.example.storage.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputUpdater;
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
import com.example.storage.model.Credentials;
import com.example.storage.model.LoginRequest;
import com.example.storage.model.UserDto;

import jakarta.ws.rs.WebApplicationException;
import lombok.extern.slf4j.Slf4j;




@Slf4j
public class UserServiceStorageProvider implements UserStorageProvider, UserLookupProvider, UserQueryProvider,
        CredentialInputValidator, CredentialInputUpdater {

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
        if (user.getUsername() != null) {
            loadedUsers.put(user.getUsername(), user); // Cache basata sullo username
            log.debug("Added user to cache with username: {}", user.getUsername());
        }
    }

    
    
    private UserModel getFromCache(String username) {
        UserModel user = loadedUsers.get(username);
        log.debug("Cache lookup for username '{}': {}", username, user != null ? "HIT" : "MISS");
        return user;
    }


    private String extractExternalId(String storageId) {
        return StorageId.externalId(storageId);
    }
    
    
//    @Override
//    public UserModel getUserById(RealmModel realm, String id) {
//        log.debug("Fetching user by ID: {}", id);
//        String externalId = extractExternalId(id);
//        try {
//            UserDto user = client.getUserById(Long.parseLong(externalId));
//            UserModel adapter = new UserAdapter(session, realm, model, user);
//            addToCache(adapter); // Aggiungi alla cache basata sullo username
//            return adapter;
//        } catch (WebApplicationException e) {
//            log.warn("User with ID '{}' not found in external service.", id);
//            return null;
//        }
//    }
    
    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        log.debug("Fetching user by ID: {}", id);

        String externalId = extractExternalId(id);

        // Cerca nella cache tramite lo username (se presente)
        for (UserModel user : loadedUsers.values()) {
            if (user.getId() != null && user.getId().equals(externalId)) {
                log.debug("Found user with ID '{}' in cache.", id);
                return user;
            }
        }

        // Se non presente in cache, chiama il servizio esterno
        try {
            UserDto userDto = client.getUserById(Long.parseLong(externalId));
            UserModel adapter = new UserAdapter(session, realm, model, userDto);
            addToCache(adapter); // Aggiunge alla cache con username
            return adapter;
        } catch (WebApplicationException e) {
            log.warn("User with ID '{}' not found in external service.", id);
            return null;
        }
    }



//
//    @Override
//    public UserModel getUserByUsername(RealmModel realm, String username) {
//        log.debug("Fetching user by username: {}", username);
//
//        UserModel adapter = getFromCache(username);
//        if (adapter != null) {
//        	 log.debug("Found user with username '{}' in cache.", username);
//            return adapter;
//        }
//
//        try {
//            UserDto user = client.getUserByUsername(username);
//            adapter = new UserAdapter(session, realm, model, user);
//            addToCache(adapter);
//            return adapter;
//        } catch (WebApplicationException e) {
//            log.warn("User with username '{}' not found in external service.", username);
//            return null;
//        }
//    }
    
    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        log.debug("Fetching user by username: {}", username);

        // Cerca nella cache
        UserModel cachedUser = getFromCache(username);
        if (cachedUser != null) {
            log.debug("Found user with username '{}' in cache.", username);
            return cachedUser;
        }

        // Se non presente in cache, chiama il servizio esterno
        try {
            UserDto userDto = client.getUserByUsername(username);
            UserModel adapter = new UserAdapter(session, realm, model, userDto);
            addToCache(adapter); // Aggiunge alla cache con username
            return adapter;
        } catch (WebApplicationException e) {
            log.warn("User with username '{}' not found in external service.", username);
            return null;
        }
    }


    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        log.debug("Fetching user by email: {}", email);

        // Cerca nella cache
        for (UserModel user : loadedUsers.values()) {
            if (user.getEmail().equals(email)) {
                log.debug("Found user with email '{}' in cache.", email);
                return user;
            }
        }

        // Se non presente in cache, chiama il servizio esterno
        try {
            UserDto userDto = client.getUserByEmail(email);
            UserModel adapter = new UserAdapter(session, realm, model, userDto);
            addToCache(adapter); // Aggiunge alla cache con username
            return adapter;
        } catch (WebApplicationException e) {
            log.warn("User with email '{}' not found in external service.", email);
            return null;
        }
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

//    private Stream<UserModel> toUserModelStream(List<UserDto> users, RealmModel realm) {
//        log.debug("Received {} users from provider", users.size());
//        return users.stream().map(user -> new UserAdapter(session, realm, model, user));
//    }
    
    private Stream<UserModel> toUserModelStream(List<UserDto> users, RealmModel realm) {
        log.debug("Mapping {} users to UserModel.", users.size());
        return users.stream().map(user -> {
            UserModel adapter = new UserAdapter(session, realm, model, user);
            addToCache(adapter);
            return adapter;
        });
    }


	@Override
	public Stream<UserModel> getGroupMembersStream(RealmModel realm, GroupModel group, Integer firstResult,
			Integer maxResults) {
		// TODO Auto-generated method stub
		return null;
	}

//  @Override
//  public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String attrName, String attrValue) {
//      log.debug("Searching for users by attribute '{}' with value '{}'.", attrName, attrValue);
//
//      return loadedUsers.values().stream()
//              .filter(user -> attrValue.equals(user.getFirstAttribute(attrName)));
//  }
	
	@Override
	public Stream<UserModel> searchForUserByUserAttributeStream(RealmModel realm, String attrName, String attrValue) {
	    log.debug("Searching for users by attribute '{}' with value '{}'.", attrName, attrValue);

	    // Cerca prima nella cache
	    Stream<UserModel> cachedUsers = loadedUsers.values().stream()
	            .filter(user -> attrValue.equals(user.getFirstAttribute(attrName)));

	    // Preparazione della query di ricerca per il sistema esterno
	    String search = attrName + ":" + attrValue; // Adatta il formato della query in base alle specifiche del client
	    List<UserDto> externalUsers = new ArrayList<>();
	    int offset = 0;
	    int limit = 50; // Dimensione del batch, può essere regolata
	    boolean hasMoreData;

	    do {
	        // Recupera un batch di utenti dal sistema esterno
	        List<UserDto> batch = client.getUsers(search, offset, limit);
	        hasMoreData = batch.size() == limit;
	        offset += limit;

	        // Aggiungi il batch ai risultati
	        externalUsers.addAll(batch);
	    } while (hasMoreData);

	    // Converte gli utenti esterni in UserModel e aggiungili alla cache
	    Stream<UserModel> externalUserStream = externalUsers.stream()
	            .map(user -> {
	                UserModel adapter = new UserAdapter(session, realm, model, user);
	                addToCache(adapter);
	                return adapter;
	            });

	    // Combina utenti in cache e quelli esterni
	    return Stream.concat(cachedUsers, externalUserStream);
	}

	
	


	@Override
	public void disableCredentialType(RealmModel realm, UserModel user, String credentialType) {
	    log.debug("Disabling credential type '{}' for user '{}'", credentialType, user.getUsername());

	    if (!supportsCredentialType(credentialType)) {
	        log.warn("Credential type '{}' is not supported for disabling.", credentialType);
	        throw new UnsupportedOperationException("Unsupported credential type: " + credentialType);
	    }

	}


	@Override
	public Stream<String> getDisableableCredentialTypesStream(RealmModel realm, UserModel user) {
	    log.debug("Fetching disableable credential types for user '{}'", user.getUsername());
	    return Stream.of(PasswordCredentialModel.TYPE); // Attualmente solo la password può essere disabilitata
	}




	
	@Override
	public boolean updateCredential(RealmModel realm, UserModel user, CredentialInput input) {
	    log.debug("Updating credentials for user '{}'", user.getUsername());

	    // Verifica se il tipo di credenziale è supportato
	    if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel)) {
	        log.warn("Unsupported credential type '{}' for user '{}'", input.getType(), user.getUsername());
	        return false;
	    }

	    UserCredentialModel credentialModel = (UserCredentialModel) input;

	    // Recupera le password vecchia e nuova dai "note"
	    String newPassword = credentialModel.getChallengeResponse();
	    String oldPassword = (String) credentialModel.getNote("oldPassword");

	    if (oldPassword == null || newPassword == null) {
	        log.warn("Both old and new passwords must be provided for user '{}'", user.getUsername());
	        throw new IllegalArgumentException("Old and new passwords are required.");
	    }

	    // Crea l'oggetto Credentials per il client
	    Credentials credentials = new Credentials();
	    credentials.setOldPassword(oldPassword);
	    credentials.setNewPassword(newPassword);

	    try {
	        // Aggiorna le credenziali tramite il client
	        String response = client.updateCredential(Long.parseLong(user.getId()), credentials);
	        log.info("Credential update response for user '{}': {}", user.getUsername(), response);
	        return true;
	    } catch (WebApplicationException e) {
	        log.error("Failed to update credentials for user '{}': {}", user.getUsername(), e.getMessage());
	        return false;
	    }
	}


	
	
	
	
	
	

  
    
    

  
}