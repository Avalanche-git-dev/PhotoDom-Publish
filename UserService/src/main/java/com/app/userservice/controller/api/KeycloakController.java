package com.app.userservice.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.userservice.entity.User;
import com.app.userservice.exception.UserException;
import com.app.userservice.model.LoginRequest;
import com.app.userservice.model.UserDto;
import com.app.userservice.model.UserMapper;
import com.app.userservice.service.UserService;

@RestController
@RequestMapping("/keycloak")
public class KeycloakController {

	@Autowired
	private UserService userService;

	

//	@GetMapping("/user")
//	public ResponseEntity<UserDto> getUserById(@RequestParam Long id) {
//		User user = userService.getUserById(id); // Se non viene trovato, lancerà UserNotFoundException dal servizio
//
//		return ResponseEntity.ok(UserMapper.toUserDto(user));
//	}
//
//	@GetMapping("/username")
//	public ResponseEntity<UserDto> getUserByUsername(@RequestParam String username) {
//		User user = userService.getUserByUsername(username);
//		return ResponseEntity.ok(UserMapper.toUserDto(user));
//	}
//
//	@GetMapping("/email")
//	public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
//		User user = userService.getUserByEmail(email);
//		return ResponseEntity.ok(UserMapper.toUserDto(user));
//	}

	@PostMapping("/login")
	public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
		User user = userService.authenticate(loginRequest);
		return ResponseEntity.ok(UserMapper.toUserDto(user));

	}

	@GetMapping("/count")
	public ResponseEntity<Integer> getUserCount() {
		int count = userService.getTotalUserCount();
		return ResponseEntity.ok(count);
	}

//	@GetMapping("/users")
//	public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) String search,
//			@RequestParam(defaultValue = "0") int first, @RequestParam(defaultValue = "15") int max) {
//		List<UserDto> users = userService.getUsers(search, first, max);
//		return ResponseEntity.ok(users);
//	}
	
//	@GetMapping("/users")
//	public ResponseEntity<Page<UserDto>> getUsers(
//	        @RequestParam(required = false) String search,
//	        @RequestParam(defaultValue = "0") int first,
//	        @RequestParam(defaultValue = "15") int max) {
//	    Page<UserDto> users = userService.getUsers(search, first, max);
//	    return ResponseEntity.ok(users);
//	}
	
	
	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> getUsersSimple(
	        @RequestParam(required = false) String search,
	        @RequestParam(defaultValue = "0") int first,
	        @RequestParam(defaultValue = "15") int max) {
	    Page<UserDto> usersPage = userService.getUsers(search, first, max);
	    return ResponseEntity.ok(usersPage.getContent()); // Restituisce solo la lista semplice
	}


	
	@GetMapping("/user")
	public ResponseEntity<UserDto> getUser(
	        @RequestParam(required = false) Long id,
	        @RequestParam(required = false) String username,
	        @RequestParam(required = false) String email) {

	    User user;

	    if (id != null) {
	        user = userService.getUserById(id);
	    } else if (username != null) {
	        user = userService.getUserByUsername(username);
	    } else if (email != null) {
	        user = userService.getUserByEmail(email);
	    } else {
	        throw new UserException("At least one parameter (id, username, email) must be provided");
	    }

	    return ResponseEntity.ok(UserMapper.toUserDto(user));
	}

	


}
