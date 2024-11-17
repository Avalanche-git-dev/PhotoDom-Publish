package com.app.userservice.model;

import com.app.userservice.entity.Qualification;
import com.app.userservice.entity.Role;
import com.app.userservice.entity.UserStatus;

public class AdminDto extends UserDto {
	
	
    private Qualification qualification;

	public AdminDto(Long id, String username, String email, UserStatus status, Role role, Qualification qualification) {
		super(id, username, email, status, role);
		this.qualification = qualification;
	}

	public Qualification getQualification() {
		return qualification;
	}

	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}

	public AdminDto() {
		super();
	}

}

