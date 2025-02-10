package com.app.userservice.model;

import java.time.LocalDate;

public class ProfileView {
	
		private Long id;
		private String firstName;
	    private String lastName;
	    private LocalDate birthday;
	    private Long age;
	    private String nickname;
	    private String telephone;
	    private Long photoProfileId;
		public ProfileView(Long id, String firstName, String lastName, LocalDate birthday, Long age, String nickname,
				String telephone, Long photoProfileId) {
			super();
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.birthday = birthday;
			this.age = age;
			this.nickname = nickname;
			this.telephone = telephone;
			this.photoProfileId = photoProfileId;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
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
		public LocalDate getBirthday() {
			return birthday;
		}
		public void setBirthday(LocalDate birthday) {
			this.birthday = birthday;
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
		public Long getPhotoProfileId() {
			return photoProfileId;
		}
		public void setPhotoProfileId(Long photoProfileId) {
			this.photoProfileId = photoProfileId;
		}
		public ProfileView() {
			super();
		}
	    
	    
	    
	    
	    
	    

}
