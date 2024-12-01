package com.example.storage.client;

import java.util.List;

import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.util.JsonSerialization;

import com.example.storage.factory.UserServiceStorageProviderFactory;
import com.example.storage.model.Credentials;
import com.example.storage.model.LoginRequest;
import com.example.storage.model.UserDto;
import com.example.storage.model.UserRegistrationRequest;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.ws.rs.WebApplicationException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;




@Slf4j
public class UserServiceClientHttp implements UserServiceClient {

    private final String baseUrl;
    private final String baseUsername;
    private final String basePassword;
    private final KeycloakSession session;

    public UserServiceClientHttp(KeycloakSession session, ComponentModel model) {
        this.baseUrl = model.get(UserServiceStorageProviderFactory.BASE_URL);
        this.baseUsername = model.get(UserServiceStorageProviderFactory.AUTH_USERNAME);//sysadmin
        this.basePassword = model.get(UserServiceStorageProviderFactory.AUTH_PASSWORD);//sysadminpw
        this.session=session;
    }

    @Override
    @SneakyThrows
    public UserDto getUserById(Long id) {
        String url = String.format("%s/user", baseUrl); // URL configurato correttamente
        log.info("Sending request to fetch user by ID: {}", id);

        SimpleHttp request = SimpleHttp.doGet(url, session)
        		.authBasic(baseUsername, basePassword)
                .param("id",String.valueOf(id)); // Passa il parametro come query string

        SimpleHttp.Response response = request.asResponse();
        
        if (response.getStatus() == 404) {
            log.warn("User with ID '{}' not found.", id);
            throw new WebApplicationException("User not found", 404);
        } else if (response.getStatus() != 200) {
            log.error("Server error for user with ID '{}'. Status: {}", id, response.getStatus());
            throw new WebApplicationException("Unexpected server error", response.getStatus());
        }

        log.info("Successfully fetched user with ID: {}", id);
        return response.asJson(UserDto.class);
    }

    @Override
    @SneakyThrows
    public UserDto authenticate(LoginRequest loginRequest) {
        String url = String.format("%s/login", baseUrl);
        log.info("Authenticating user: {}", loginRequest.getUsername());

        SimpleHttp.Response response = SimpleHttp.doPost(url, session)
        		.authBasic(baseUsername, basePassword)
        		.header("Content-Type", "application/json")
                .json(loginRequest)
                .asResponse();

        if (response.getStatus() == 200) {
            log.info("Authentication successful for user: {}", loginRequest.getUsername());
            return response.asJson(UserDto.class);
        } else if (response.getStatus() == 401) {
            log.warn("Authentication failed for user: {}", loginRequest.getUsername());
            throw new WebApplicationException("Invalid credentials", 401);
        } else {
            log.error("Unexpected server error during authentication for user: {}. Status: {}", 
                      loginRequest.getUsername(), response.getStatus());
            throw new WebApplicationException("Unexpected server error", response.getStatus());
        }
    }

    @Override
    @SneakyThrows
    public List<UserDto> getAllUsers() {
        String url = String.format("%s", baseUrl); // Base URL gestisce il suffisso "users"
        log.info("Fetching all users from: {}", url);

        SimpleHttp.Response response = SimpleHttp.doGet(url, session).authBasic(baseUsername, basePassword).asResponse();

        if (response.getStatus() == 200) {
            log.info("Successfully fetched all users.");
			return JsonSerialization.readValue(response.asString(), new TypeReference<List<UserDto>>() {});
        } else {
            log.error("Failed to fetch users. Server error: {}", response.getStatus());
            throw new WebApplicationException("Unexpected server error", response.getStatus());
        }
    }

    @Override
    @SneakyThrows
    public UserDto getUserByEmail(String email) {
        String url = String.format("%s/email", baseUrl); // Aggiunge solo la desinenza "/email"
        log.info("Fetching user by email: {}", email);

        SimpleHttp request = SimpleHttp.doGet(url, session).authBasic(baseUsername, basePassword)
                .param("email", email); // Passa il parametro come query string

        SimpleHttp.Response response = request.asResponse();

        if (response.getStatus() == 404) {
            log.warn("User with email '{}' not found.", email);
            throw new WebApplicationException("User not found", 404);
        } else if (response.getStatus() != 200) {
            log.error("Server error for email '{}'. Status: {}", email, response.getStatus());
            throw new WebApplicationException("Unexpected server error", response.getStatus());
        }

        log.info("Successfully fetched user with email: {}", email);
        return response.asJson(UserDto.class);
    }

    @Override
    @SneakyThrows
    public UserDto getUserByUsername(String username) {
        String url = String.format("%s/username", baseUrl); // Aggiunge solo la desinenza "/username"
        log.info("Fetching user by username: {}", username);

        SimpleHttp request = SimpleHttp.doGet(url, session)
        		.authBasic(baseUsername, basePassword)
                .param("username", username); // Passa il parametro come query string

        SimpleHttp.Response response = request.asResponse();

        if (response.getStatus() == 404) {
            log.warn("User with username '{}' not found.", username);
            throw new WebApplicationException("User not found", 404);
        } else if (response.getStatus() != 200) {
            log.error("Server error for username '{}'. Status: {}", username, response.getStatus());
            throw new WebApplicationException("Unexpected server error", response.getStatus());
        }

        log.info("Successfully fetched user with username: {}", username);
        return response.asJson(UserDto.class);
    }


//    
//    
//    @Override
//    @SneakyThrows
//    public List<UserDto> getAllUsersByP(int page, int size) {
//        String url = String.format("%s/users?page=%d&size=%d", baseUrl, page, size);
//        log.info("Fetching users with pagination: page={}, size={}", page, size);
//
//        SimpleHttp.Response response = SimpleHttp.doGet(url, session).asResponse();
//
//        if (response.getStatus() == 200) {
//            log.info("Successfully fetched users.");
//            return JsonSerialization.readValue(response.asString(), new TypeReference<List<UserDto>>() {});
//        } else {
//            log.error("Failed to fetch users. Status: {}", response.getStatus());
//            throw new WebApplicationException("Unexpected server error", response.getStatus());
//        }
//    }
    
    
    
    @SneakyThrows
    public int getTotalUserCount() {
        String url = String.format("%s/count", baseUrl);
        log.info("Fetching total user count.");

        SimpleHttp.Response response = SimpleHttp.doGet(url, session)
        		.authBasic(baseUsername, basePassword)
        		.asResponse();

        if (response.getStatus() == 200) {
            log.info("Successfully fetched user count.");
            return JsonSerialization.readValue(response.asString(), Integer.class);
        } else {
            log.error("Failed to fetch user count. Status: {}", response.getStatus());
            throw new WebApplicationException("Unexpected server error", response.getStatus());
        }
    }

    @SneakyThrows
    public List<UserDto> getUsers(String search, Integer firstResult, Integer maxResults) {
        String url = String.format("%s/users?search=%s&first=%d&max=%d", 
                                    baseUrl, 
                                    search != null ? search : "", 
                                    firstResult != null ? firstResult : 0, 
                                    maxResults != null ? maxResults : 10);
        log.info("Fetching users with search={}, firstResult={}, maxResults={}", search, firstResult, maxResults);

        SimpleHttp.Response response = SimpleHttp.doGet(url, session)
        		.authBasic(baseUsername, basePassword)
        		.asResponse();

        if (response.getStatus() == 200) {
            log.info("Successfully fetched users.");
            return JsonSerialization.readValue(response.asString(), new TypeReference<List<UserDto>>() {});
        } else {
            log.error("Failed to fetch users. Status: {}", response.getStatus());
            throw new WebApplicationException("Unexpected server error", response.getStatus());
        }
    }


    @Override
    @SneakyThrows
    public String deleteUser(Long id) {
        String url = String.format("%s/delete", baseUrl); // Solo il percorso base
        log.info("Sending request to delete user with ID: {}", id);

        SimpleHttp.Response response = SimpleHttp.doDelete(url, session)
                .authBasic(baseUsername, basePassword)
                .param("id", String.valueOf(id)) // Aggiunge il parametro come query
                .asResponse();

        if (response.getStatus() == 200) {
            log.info("Successfully deleted user with ID: {}", id);
            return "User deleted successfully.";
        } else if (response.getStatus() == 404) {
            log.warn("User with ID '{}' not found.", id);
            throw new WebApplicationException("User not found", 404);
        } else {
            log.error("Failed to delete user with ID '{}'. Server returned: {}", id, response.getStatus());
            throw new WebApplicationException("Unexpected server error", response.getStatus());
        }
    }

    @Override
    @SneakyThrows
    public UserDto register(UserRegistrationRequest user) {
        String url = String.format("%s/register", baseUrl); // Aggiunge il suffisso "/register"
        log.info("Sending request to register user: {}", user.getUsername());

        SimpleHttp.Response response = SimpleHttp.doPost(url, session)
                .authBasic(baseUsername, basePassword)
                .header("Content-Type", "application/json")
                .json(user)
                .asResponse();

        if (response.getStatus() == 201) { // 201 Created
            log.info("Successfully registered user: {}", user.getUsername());
            return response.asJson(UserDto.class);
        } else if (response.getStatus() == 409) { // Conflict
            log.warn("Conflict while registering user: {}", user.getUsername());
            throw new WebApplicationException("User already exists", 409);
        } else {
            log.error("Failed to register user '{}'. Server returned: {}", user.getUsername(), response.getStatus());
            throw new WebApplicationException("Unexpected server error", response.getStatus());
        }
    }

	@Override
	@SneakyThrows
	public String updateCredential(Long id, Credentials credentials) {
	    String url = String.format("%s/credentials", baseUrl); // Solo il percorso base
	    log.info("Sending request to update credentials for user with ID: {}", id);

	    SimpleHttp.Response response = SimpleHttp.doPut(url, session)
	            .authBasic(baseUsername, basePassword)
	            .param("id", String.valueOf(id)) // Aggiunge il parametro come query
	            .header("Content-Type", "application/json")
	            .json(credentials)
	            .asResponse();

	    if (response.getStatus() == 200) {
	        log.info("Successfully updated credentials for user with ID: {}", id);
	        return "Password updated successfully.";
	    } else if (response.getStatus() == 400) {
	        log.warn("Bad request while updating credentials for user with ID: {}", id);
	        throw new WebApplicationException("Invalid request data", 400);
	    } else {
	        log.error("Failed to update credentials for user '{}'. Server returned: {}", id, response.getStatus());
	        throw new WebApplicationException("Unexpected server error", response.getStatus());
	    }
	}



    
    
    
    
   
    }




