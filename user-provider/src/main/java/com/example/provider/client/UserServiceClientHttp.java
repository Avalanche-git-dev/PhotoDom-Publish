package com.example.provider.client;

import java.util.List;

import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.util.JsonSerialization;

import com.example.provider.UserServiceStorageProviderFactory;
import com.example.provider.dto.LoginRequest;
import com.example.provider.dto.UserDto;
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
		this.baseUsername = model.get(UserServiceStorageProviderFactory.AUTH_USERNAME);// sysadmin
		this.basePassword = model.get(UserServiceStorageProviderFactory.AUTH_PASSWORD);// sysadminpw
		this.session = session;
	}

	@Override
	@SneakyThrows
	public UserDto authenticate(LoginRequest loginRequest) {
		String url = String.format("%s/login", baseUrl);
		log.info("Authenticating user: {}", loginRequest.getUsername());

		SimpleHttp.Response response = SimpleHttp.doPost(url, session).authBasic(baseUsername, basePassword)
				.header("Content-Type", "application/json").json(loginRequest).asResponse();

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

	@SneakyThrows
	public int getTotalUserCount() {
		String url = String.format("%s/count", baseUrl);
		log.info("Call total user count.");

		SimpleHttp.Response response = SimpleHttp.doGet(url, session).authBasic(baseUsername, basePassword)
				.asResponse();

		if (response.getStatus() == 200) {
			log.info("Successfully call user count.");
			return JsonSerialization.readValue(response.asString(), Integer.class);
		} else {
			log.error("Failed to call user count. Status: {}", response.getStatus());
			throw new WebApplicationException("Unexpected server error", response.getStatus());
		}
	}

	@Override
	@SneakyThrows
	public UserDto getUser(Long id, String username, String email) {
		StringBuilder urlBuilder = new StringBuilder(baseUrl + "/user?");
		if (id != null) {
			urlBuilder.append("id=").append(id);
		} else if (username != null) {
			urlBuilder.append("username=").append(username);
		} else if (email != null) {
			urlBuilder.append("email=").append(email);
		} else {
			throw new IllegalArgumentException("At least one parameter (id, username, or email) must be provided");
		}

		String url = urlBuilder.toString();
		log.info("Call to user-service: Fetching user with parameters [id={}, username={}, email={}]", id, username,
				email);

		SimpleHttp.Response response = SimpleHttp.doGet(url, session).authBasic(baseUsername, basePassword)
				.asResponse();

		if (response.getStatus() == 200) {
			log.info("Call to user-service: Successfully retrieved user.");
			return response.asJson(UserDto.class);
		} else {
			log.error("Call to user-service failed. Status: {}", response.getStatus());
			throw new WebApplicationException("Unexpected server error", response.getStatus());
		}
	}

	@Override
	@SneakyThrows
	public List<UserDto> getUsers(String search, Integer firstResult, Integer maxResults) {
		String url = String.format("%s/users?search=%s&first=%d&max=%d", baseUrl, search != null ? search : "",
				firstResult != null ? firstResult : 0, maxResults != null ? maxResults : 10);

		log.info("Call to user-service: Paginated user fetch with search={}, firstResult={}, maxResults={}", search,
				firstResult, maxResults);

		SimpleHttp.Response response = SimpleHttp.doGet(url, session).authBasic(baseUsername, basePassword)
				.asResponse();

		if (response.getStatus() == 200) {
			log.info("Call to user-service: Successfully fetched users.");
			return JsonSerialization.readValue(response.asString(), new TypeReference<List<UserDto>>() {
			});
		} else {
			log.error("Call to user-service failed. Status: {}", response.getStatus());
			throw new WebApplicationException("Unexpected server error", response.getStatus());
		}
	}


}
