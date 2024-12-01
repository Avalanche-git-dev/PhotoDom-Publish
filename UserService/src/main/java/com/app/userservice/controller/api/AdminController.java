package com.app.userservice.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.userservice.model.UserDto;
import com.app.userservice.service.UserService;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping("/{id}/ban/s")
    public ResponseEntity<String> banUser(@PathVariable Long id) {
        userService.banUser(id);
        return ResponseEntity.ok("User with ID " + id + " has been banned.");
    }

    @PostMapping("/{id}/ban/r")
    public ResponseEntity<String> removeBanUser(@PathVariable Long id) {
        userService.unbanUser(id);
        return ResponseEntity.ok("User with ID " + id + " has been unbanned.");
    }

    @PostMapping("/{id}/admin/a")
    public ResponseEntity<String> addAdmin(@PathVariable Long id) {
        userService.nominateAdmin(id);
        return ResponseEntity.ok("User with ID " + id + " has been promoted to Admin.");
    }
    
    
    
    @GetMapping("/banned")
    public ResponseEntity<List<UserDto>> getBannedUsers() {
        List<UserDto> bannedUsers = userService.getAllBannedUsers();
        return ResponseEntity.ok(bannedUsers);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<UserDto>> getInactiveUsers() {
        List<UserDto> inactiveUsers = userService.getAllInactiveUsers();
        return ResponseEntity.ok(inactiveUsers);
    }
    }
