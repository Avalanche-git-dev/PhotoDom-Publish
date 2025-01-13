package com.example.provider.client;

import java.util.List;

import com.example.provider.dto.LoginRequest;
import com.example.provider.dto.UserDto;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface UserServiceClient {

	UserDto authenticate(LoginRequest loginRequest);

	List<UserDto> getUsers(String search, Integer firstResult, Integer maxResults);

	int getTotalUserCount();

	UserDto getUser(Long id, String username, String email);

}