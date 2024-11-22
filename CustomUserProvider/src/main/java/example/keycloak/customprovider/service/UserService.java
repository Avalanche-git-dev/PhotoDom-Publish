package example.keycloak.customprovider.service;


import java.util.Optional;

import org.springframework.stereotype.Service;

import example.keycloak.customprovider.entity.User;
import example.keycloak.customprovider.repository.UserRepository;

@Service
public class UserService {
	
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findById(Long id) {
    	return userRepository.findById(id);
    }

	public void save(User user) {
		userRepository.save(user);
	}
}
