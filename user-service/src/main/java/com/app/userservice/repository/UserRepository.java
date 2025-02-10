package com.app.userservice.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.userservice.entity.User;
import com.app.userservice.entity.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByStatus(UserStatus status);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Optional<User> findByNickname(String nickname);
	Optional<User> findByTelephone(String telephone);
	Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email, Pageable pageable);
    
    
	
	
	
	 @Query("SELECT u FROM User u WHERE " +
	           "(:nickname IS NULL OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :nickname, '%'))) AND " +
	           "(:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
	           "(:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))")
	    List<User> findUsersByFilter(String nickname, String firstName, String lastName);
}
