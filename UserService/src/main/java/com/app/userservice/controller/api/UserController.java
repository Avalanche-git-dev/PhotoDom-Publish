package com.app.userservice.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.userservice.entity.User;
import com.app.userservice.model.Credentials;
import com.app.userservice.model.UserDto;
import com.app.userservice.model.UserMapper;
import com.app.userservice.service.UserService;

//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//	@Autowired
//	private UserService userService;
//
//	@Autowired
//	private KafkaProducerService kafkaProducerService;
//
//	@GetMapping
//	public ResponseEntity<List<UserDto>> getAllUsers() {
//		List<UserDto> users = userService.getAllUsers();
//		System.out.println(users);
//		return ResponseEntity.ok(users);
//	}
//
//	@GetMapping("/profile")
//	public ResponseEntity<UserDto> getUserById(@RequestParam Long id) {
//		User user = userService.getUserById(id);
//
//		String message = "User: " + UserMapper.toUserDto(user).toString();
//		kafkaProducerService.sendMessage("user-details-topic", message);
//		return ResponseEntity.ok(UserMapper.toUserDto(user));
//	}
//
//	@PostMapping("/register")
//	public ResponseEntity<UserDto> createUser(@RequestBody User user) {
//		User createdUser = userService.createUser(user);
//		return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toUserDto(createdUser));
//	}
//
//	@PutMapping("/profile/update")
//	public ResponseEntity<UserDto> updateUser(/* @RequestParam Long id, */ @RequestBody UserDto userDetails) {
//		User updatedUser = userService.updateUser(/* id, */ userDetails);
//		return ResponseEntity.ok(UserMapper.toUserDto(updatedUser));
//	}
//
//	@DeleteMapping("/delete")
//	public ResponseEntity<String> deleteUser(@RequestParam Long id) {
//		userService.deleteUser(id);
//		return ResponseEntity.ok("User deleted successfully.");
//	}
//
//	@PutMapping("/credentials/update")
//	public ResponseEntity<String> changePsw(/* @RequestParam Long id, */ @RequestBody Credentials credentials) {
//		userService.updateCredentials(/* id, */credentials);
//		return ResponseEntity.ok("Password updated successfully.");
//	}
//
//	
//	
//	
//	
//	
//	
//	
//	
//	
//	
//
//
//}








@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

//    @Autowired
//    private KafkaProducerService kafkaProducerService;
    
    
    
	@GetMapping
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> users = userService.getAllUsers();
		return ResponseEntity.ok(users);
	}

//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getAllUsers() {
//        List<UserDto> users = userService.getAllUsers();
//        return ResponseEntity.ok(Map.of(
//                "success", true,
//                "message", "Users retrieved successfully",
//                "users", users,
//                "status", HttpStatus.OK.value()
//        ));
//    }
//
//    @GetMapping("/profile")
//    public ResponseEntity<Map<String, Object>> getUserById(@RequestParam Long id) {
//        User user = userService.getUserById(id);
//        String message = "User: " + UserMapper.toUserDto(user).toString();
//        kafkaProducerService.sendMessage("user-details-topic", message);
//        return ResponseEntity.ok(Map.of(
//                "success", true,
//                "message", "User retrieved successfully",
//                "user", UserMapper.toUserDto(user),
//                "status", HttpStatus.OK.value()
//        ));
//    }
    
    
	@GetMapping("/profile")
	public ResponseEntity<UserDto> getUserById(@RequestParam Long id) {
		User user = userService.getUserById(id);

//		String message = "User: " + UserMapper.toUserDto(user).toString();
		return ResponseEntity.ok(UserMapper.toUserDto(user));
	}
    
    
    

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "User registered successfully",
                "userId", createdUser.getId(),
                "status", HttpStatus.CREATED.value()
        ));
    }

    @PutMapping("/profile/update")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestBody UserDto userDetails) {
        User updatedUser = userService.updateUser(userDetails);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User updated successfully",
                "userId", updatedUser.getId(),
                "status", HttpStatus.OK.value()
        ));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User deleted successfully",
                "status", HttpStatus.OK.value()
        ));
    }

    @PutMapping("/credentials/update")
    public ResponseEntity<Map<String, Object>> changePsw(@RequestBody Credentials credentials) {
        userService.updateCredentials(credentials);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Password updated successfully",
                "status", HttpStatus.OK.value()
        ));
    }
}
