package com.app.userservice.model;

import java.time.LocalDate;

import com.app.userservice.entity.Qualification;
import com.app.userservice.entity.Role;
import com.app.userservice.entity.UserStatus;

public class AdminDto extends UserDto {
	
	
    private Qualification qualification;


	public Qualification getQualification() {
		return qualification;
	}

	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}

	public AdminDto() {
		super();
	}

	public AdminDto(Long id, String username, String email, String firstName, String lastName, LocalDate birthday,
			Long age, String nickname, String telephone, UserStatus status, Role role, Qualification qualification) {
		super(id, username, email, firstName, lastName, birthday, age, nickname, telephone, status, role);
		this.qualification = qualification;
	}

	
	
	
	

}

