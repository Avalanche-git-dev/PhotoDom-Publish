package com.app.userservice.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.userservice.entity.User;
import com.app.userservice.kafka.KafkaProducerService;
import com.app.userservice.model.LoginRequest;
import com.app.userservice.model.UserDto;
import com.app.userservice.model.UserMapper;
import com.app.userservice.service.UserService;

@RestController
@RequestMapping("/keycloak")
public class KeycloakController {

	@Autowired
	private UserService userService;

	@Autowired
	private KafkaProducerService kafkaProducerService;

	@GetMapping("/user")
	public ResponseEntity<UserDto> getUserById(@RequestParam Long id) {
		User user = userService.getUserById(id); // Se non viene trovato, lancerà UserNotFoundException dal servizio

		String message = "User details retrieved: " + UserMapper.toUserDto(user).toString();
		kafkaProducerService.sendMessage("user-details-topic", message);
		return ResponseEntity.ok(UserMapper.toUserDto(user));
	}

	@GetMapping("/username")
	public ResponseEntity<UserDto> getUserByUsername(@RequestParam String username) {
		User user = userService.getUserByUsername(username);
		return ResponseEntity.ok(UserMapper.toUserDto(user));
	}

	@GetMapping("/email")
	public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
		User user = userService.getUserByEmail(email);
		return ResponseEntity.ok(UserMapper.toUserDto(user));
	}

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

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) String search,
			@RequestParam(defaultValue = "0") int first, @RequestParam(defaultValue = "15") int max) {
		List<UserDto> users = userService.getUsers(search, first, max);
		return ResponseEntity.ok(users);
	}

//	@PutMapping("/credentials")
//	public ResponseEntity<String> changePsw(@RequestParam Long id, @RequestBody Credentials credentials) {
//		userService.updateCredentials(id, credentials);
//		return ResponseEntity.ok("Password updated successfully.");
//	}
//	
//	@DeleteMapping("/delete")
//	public ResponseEntity<String> deleteUser(@RequestParam Long id) {
//	    userService.deleteUser(id); // Gestisce eventuali eccezioni come UserNotFoundException
//	    return ResponseEntity.ok("User deleted successfully.");
//	}
	
//	@PostMapping("/register")
//	public ResponseEntity<UserDto> createUser(@RequestBody User user) {
//		User createdUser = userService.createUser(user); // Può lanciare DuplicateUsernameException o
//															// DuplicateEmailException
//		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserDto(createdUser));
//	}

}
