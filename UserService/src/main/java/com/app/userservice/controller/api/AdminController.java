package com.app.userservice.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.userservice.model.UserDto;
import com.app.userservice.model.UserResponse;
import com.app.userservice.service.UserService;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private UserService userService;

    // **Banna un utente**
    @PostMapping("/ban/add")
    public UserResponse<Void> banUser(@RequestParam Long id) {
        userService.banUser(id);
        return UserResponse.success(
            "User with ID " + id + " has been banned.",
            HttpStatus.OK
        );
    }

    // **Rimuove il ban da un utente**
    @PostMapping("/ban/remove")
    public UserResponse<Void> removeBanUser(@RequestParam Long id) {
        userService.unbanUser(id);
        return UserResponse.success(
            "User with ID " + id + " has been unbanned.",
            HttpStatus.OK
        );
    }

    // **Promuove un utente ad Admin**
    @PostMapping("/promote/admin")
    public UserResponse<Void> addAdmin(@RequestParam Long id) {
        userService.nominateAdmin(id);
        return UserResponse.success(
            "User with ID " + id + " has been promoted to Admin.",
            HttpStatus.OK
        );
    }

    // **Recupera tutti gli utenti bannati**
    @GetMapping("/banned")
    public UserResponse<List<UserDto>> getBannedUsers() {
        List<UserDto> bannedUsers = userService.getAllBannedUsers();
        return UserResponse.success(
            "List of banned users retrieved successfully.",
            HttpStatus.OK,
            bannedUsers
        );
    }

    // **Recupera tutti gli utenti inattivi**
    @GetMapping("/inactive")
    public UserResponse<List<UserDto>> getInactiveUsers() {
        List<UserDto> inactiveUsers = userService.getAllInactiveUsers();
        return UserResponse.success(
            "List of inactive users retrieved successfully.",
            HttpStatus.OK,
            inactiveUsers
        );
    }
}

