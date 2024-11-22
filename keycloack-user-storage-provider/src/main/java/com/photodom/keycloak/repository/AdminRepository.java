package com.photodom.keycloak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.photodom.keycloak.mapping.Admin;
import com.photodom.keycloak.utility.Qualification;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByQualification(Qualification qualification);
}
