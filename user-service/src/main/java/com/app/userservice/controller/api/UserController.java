package com.app.userservice.controller.api;

import java.util.List;

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
import com.app.userservice.model.ProfileView;
import com.app.userservice.model.UserDto;
import com.app.userservice.model.UserFilter;
import com.app.userservice.model.UserMapper;
import com.app.userservice.model.UserResponse;
import com.app.userservice.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Recupera tutti gli utenti
    @GetMapping
    public ResponseEntity<UserResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(UserResponse.success("Users retrieved successfully", HttpStatus.OK, users));
    }

    // Recupera il profilo di un utente tramite ID
    @GetMapping("/profile")
    public ResponseEntity<UserResponse<UserDto>> getUserById(@RequestParam Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserResponse.success("User retrieved successfully", HttpStatus.OK, UserMapper.toUserDto(user)));
    }


    
    
    
    // Registra un nuovo utente
    @PostMapping("/register")
    public ResponseEntity<UserResponse<Void>> createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                UserResponse.success("User registered successfully", HttpStatus.CREATED)
        );
    }

    // Aggiorna il profilo dell'utente
    @PutMapping("/profile/update")
    public ResponseEntity<UserResponse<Long>> updateUser(@RequestBody UserDto userDetails) {
        User updatedUser = userService.updateUser(userDetails);
        return ResponseEntity.ok(
                UserResponse.success("User updated successfully", HttpStatus.OK, updatedUser.getId())
        );
    }

    // Elimina un utente tramite ID
    @DeleteMapping("/delete")
    public ResponseEntity<UserResponse<Void>> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(UserResponse.success("User deleted successfully", HttpStatus.OK));
    }

    // Aggiorna la password dell'utente
    @PutMapping("/credentials/update")
    public ResponseEntity<UserResponse<Void>> changePassword(@RequestBody Credentials credentials) {
        userService.updateCredentials(credentials);
        return ResponseEntity.ok(UserResponse.success("Password updated successfully", HttpStatus.OK));
    }
    
    
    @GetMapping("/profile/view")
    public ResponseEntity<UserResponse<ProfileView>> getProfileView(@RequestParam Long id) {
        // Chiama il servizio per ottenere il profilo limitato
        ProfileView profile = userService.getProfile(id);

        // Restituisce una risposta strutturata utilizzando UserResponse
        return ResponseEntity.ok(
                UserResponse.success("Profile retrieved successfully", HttpStatus.OK, profile)
        );
    }
    
    
    @GetMapping("/nickname")
    public ResponseEntity<UserResponse<String>> getNickname(@RequestParam Long id) {
        String nickname = userService.getNicknameById(id);
        return ResponseEntity.ok(
                UserResponse.success("Nickname retrieved successfully", HttpStatus.OK, nickname)
        );
    }

    // Recupera i nickname di una lista di utenti
    @GetMapping("/nicknames")
    public ResponseEntity<UserResponse<List<String>>> getNicknamesBatch(@RequestBody List<Long> ids) {
        List<String> nicknames = userService.getNicknamesByIds(ids);
        return ResponseEntity.ok(
                UserResponse.success("Nicknames retrieved successfully", HttpStatus.OK, nicknames)
        );
    }
    
    
    @PostMapping("/search")
    public ResponseEntity<UserResponse<List<ProfileView>>> searchUsers(@RequestBody UserFilter filter) {
        List<ProfileView> profiles = userService.findProfileViewsByFilter(filter);
        return ResponseEntity.ok(
            UserResponse.success("Profiles retrieved successfully.", HttpStatus.OK, profiles)
        );
    }
    
    }



