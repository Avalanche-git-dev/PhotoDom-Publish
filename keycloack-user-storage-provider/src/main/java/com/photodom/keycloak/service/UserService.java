package com.photodom.keycloak.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.photodom.keycloak.mapping.Admin;
import com.photodom.keycloak.mapping.User;
import com.photodom.keycloak.repository.UserRepository;
import com.photodom.keycloak.utility.Qualification;
import com.photodom.keycloak.utility.Role;
import com.photodom.keycloak.utility.UserStatus;

@Service
@Transactional
public class UserService {
	
    private  UserRepository userRepository;
    private  BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

	public UserService() {
		super();
	}


	// Recupera un utente o un admin in base all'username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Recupera un admin in base all'username
//    public Optional<Admin> findAdminByUsername(String username) {
//        return userRepository.findAdminByUsername(username);
//    }

    // Crea un nuovo utente
    public User createUser(String username, String rawPassword, String email, Role role, UserStatus status) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(status);

        return userRepository.save(user);
    }

    // Crea un nuovo admin
    public Admin createAdmin(String username, String rawPassword, String email, UserStatus status, Qualification qualification) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        admin.setEmail(email);
        admin.setRole(Role.ADMIN);
        admin.setStatus(status);
        admin.setQualification(qualification);

        return userRepository.save(admin);
    }

    // Metodo di autenticazione
    public boolean authenticate(String username, String rawPassword) {
        Optional<User> userOpt = findByUsername(username);

        if (userOpt.isEmpty()) {
            return false; // Utente non trovato
        }

        User user = userOpt.get();

        if (user.getStatus() == UserStatus.BANNED) {
            throw new IllegalStateException("L'utente è stato bannato e non può autenticarsi.");
        }

        // Verifica della password
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

	public Optional<User> findById(long id) {
		return userRepository.findById(id);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
