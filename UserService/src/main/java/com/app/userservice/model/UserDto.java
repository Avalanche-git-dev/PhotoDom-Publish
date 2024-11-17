package com.app.userservice.model;

import com.app.userservice.entity.Role;
import com.app.userservice.entity.UserStatus;

public class UserDto {
	
	
    private Long id;
    private String username;
    private String email;
    private UserStatus status;
    private Role role;
    
    
	public UserDto(Long id, String username, String email, UserStatus status, Role role) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.status = status;
		this.role = role;
	}
	
	public UserDto() {
		super();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

    
    
    
    
}

