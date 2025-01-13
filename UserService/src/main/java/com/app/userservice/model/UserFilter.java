package com.app.userservice.model;

public class UserFilter {
	    private String nickname;
	    private String firstName;
	    private String lastName;
		public String getNickname() {
			return nickname;
		}
		public void setNickname(String nickname) {
			this.nickname = nickname;
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
		public UserFilter() {
			super();
			// TODO Auto-generated constructor stub
		}
		public UserFilter(String nickname, String firstName, String lastName) {
			super();
			this.nickname = nickname;
			this.firstName = firstName;
			this.lastName = lastName;
		}

}
