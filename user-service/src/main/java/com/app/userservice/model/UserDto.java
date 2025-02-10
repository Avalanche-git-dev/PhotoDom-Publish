package com.app.userservice.model;

import java.time.LocalDate;

import com.app.userservice.entity.Role;
import com.app.userservice.entity.UserStatus;


public class UserDto {
	

	/**
	 * 
	 */
	private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Long age;
    private String nickname;
    private String telephone;
    private Long photoProfileId;
    
    private UserStatus status;
    private Role role;
    
   
    
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public UserDto(Long id, String username, String email, String firstName, String lastName, LocalDate birthday,
			Long age, String nickname, String telephone, UserStatus status, Role role, Long photoProfileId) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.age = age;
		this.nickname = nickname;
		this.telephone = telephone;
		this.status = status;
		this.role = role;
		this.photoProfileId = photoProfileId;
	}

	public Long getPhotoProfileId() {
		return photoProfileId;
	}

	public void setPhotoProfileId(Long photoProfileId) {
		this.photoProfileId = photoProfileId;
	}

    
    
    
}

