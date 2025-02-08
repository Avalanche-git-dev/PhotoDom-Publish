package com.app.userservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.userservice.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	@Modifying
	@Query(value = "INSERT INTO admins (id, qualification) VALUES (:id, :qualification)", nativeQuery = true)
	void insertAdminDetails(@Param("id") Long id, @Param("qualification") String qualification);

}
