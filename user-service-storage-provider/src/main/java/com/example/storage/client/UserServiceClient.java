package com.example.storage.client;

import java.util.List;

import com.example.storage.model.Credentials;
import com.example.storage.model.LoginRequest;
import com.example.storage.model.UserDto;
import com.example.storage.model.UserRegistrationRequest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserServiceClient {

	UserDto getUserById(Long id);

	UserDto getUserByEmail(String email);

	UserDto getUserByUsername(String username);

	UserDto authenticate(LoginRequest loginRequest);

	List<UserDto> getAllUsers();

	List<UserDto> getUsers(String search, Integer firstResult, Integer maxResults);

	int getTotalUserCount();

	UserDto register(UserRegistrationRequest user);

	String updateCredential(Long id, Credentials credentials);

	String deleteUser(Long id);

}
