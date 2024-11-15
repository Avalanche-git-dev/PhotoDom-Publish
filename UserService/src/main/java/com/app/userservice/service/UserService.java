package com.app.userservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.userservice.entity.Role;
import com.app.userservice.entity.User;
import com.app.userservice.exception.DuplicateEmailException;
import com.app.userservice.exception.DuplicateUsernameException;
import com.app.userservice.exception.InvalidFieldException;
import com.app.userservice.exception.UserNotFoundException;
import com.app.userservice.model.UserDto;
import com.app.userservice.model.UserMapper;
import com.app.userservice.repository.UserRepository;
import com.app.userservice.utility.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    
    @Autowired
    private PasswordEncoder passwordEncoder; // Per codificare e verificare le password

    @Autowired
    private JwtUtil jwtUtil;
    
    public List<UserDto> getAllUsers() {
        List<UserDto> users = userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        return users;
    }
    
    @Cacheable("users")
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already exists: " + user.getUsername());
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setBlocked(false);
        user.setRole(Role.USER);
        return userRepository.save(user);
    }
    
    
    @CacheEvict(value = "users", key = "#id")
    public User updateUser(Long id, UserDto userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (userDetails.getUsername() != null && !userDetails.getUsername().isEmpty()) {
            if (!user.getUsername().equals(userDetails.getUsername()) &&
                userRepository.findByUsername(userDetails.getUsername()).isPresent()) {
                throw new DuplicateUsernameException("Username already exists: " + userDetails.getUsername());
            }
            user.setUsername(userDetails.getUsername());
        }

        if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
            if (!user.getEmail().equals(userDetails.getEmail()) &&
                userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
                throw new DuplicateEmailException("Email already exists: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());
        }

        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }

        user.setBlocked(userDetails.isBlocked());
        return userRepository.save(user);
    }
    
    
    
    @CacheEvict(value = "users", key = "#id") 
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    
    
    public String authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidFieldException("Invalid username or password");
        }

        return jwtUtil.generateToken(user.getUsername());
    }

}
