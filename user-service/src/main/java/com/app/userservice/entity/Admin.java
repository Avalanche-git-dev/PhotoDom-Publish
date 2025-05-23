package com.app.userservice.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin extends User {
	
	

	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
    private Qualification qualification;
    
    
    
    

	


	public Admin(Long id, String username, String password, String email, String firstName, String lastName,
			LocalDate birthday, String nickname, String telephone, Long photoProfileId, Role role, UserStatus status,
			Qualification qualification) {
		super(id, username, password, email, firstName, lastName, birthday, nickname, telephone, photoProfileId, role,
				status);
		this.qualification = qualification;
	}

	public Admin() {
		super();
	}

	public Qualification getQualification() {
		return qualification;
	}

	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}

	@Override
	public String toString() {
		return "Admin [qualification=" + qualification + ", " + super.toString() + "]";
	}


    
    
}
