package example.keycloak.customprovider.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import example.keycloak.customprovider.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
}
