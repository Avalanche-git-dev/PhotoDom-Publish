package com.app.userservice.controller.api;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.userservice.configuration.KafkaProducerService;
import com.app.userservice.entity.User;
import com.app.userservice.model.LoginRequest;
import com.app.userservice.model.UserDto;
import com.app.userservice.model.UserMapper;
import com.app.userservice.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private KafkaProducerService kafkaProducerService;
    

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        System.out.println(users);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id); // Se non viene trovato, lancerà UserNotFoundException dal servizio
        
        String message = "User details retrieved: " + UserMapper.toUserDto(user).toString();
        kafkaProducerService.sendMessage("user-details-topic", message);
        return ResponseEntity.ok(UserMapper.toUserDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user); // Può lanciare DuplicateUsernameException o DuplicateEmailException
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserDto(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDetails) {
        User updatedUser = userService.updateUser(id, userDetails); // Può lanciare UserNotFoundException o InvalidFieldException
        return ResponseEntity.ok(UserMapper.toUserDto(updatedUser));
    }
    
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id); // Può lanciare UserNotFoundException
        return ResponseEntity.noContent().build();
    }
    
    
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String token = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }

}

