package com.photodom.keycloak.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.photodom.keycloak.mapping.User;




//@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByUsername(String username);
//    Optional<User> findByEmail (String email);
//   // @Query("SELECT a FROM Admin a WHERE a.username = :username")
//    Optional<Admin> findAdminByUsername(/*@Param("username")*/ String username);
//}



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
