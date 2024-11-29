package com.example.storage.client;


import java.util.List;

import com.example.storage.model.LoginRequest;
import com.example.storage.model.UserDto;

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
    public int getTotalUserCount();


    
}

    	
    	

