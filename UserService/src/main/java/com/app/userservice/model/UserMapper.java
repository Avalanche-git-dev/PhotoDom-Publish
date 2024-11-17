package com.app.userservice.model;

import com.app.userservice.entity.Admin;
import com.app.userservice.entity.User;

public class UserMapper {

    public static UserDto toUserDto(User user) {
    	 if (user == null) {
             return null; 
         }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setStatus(user.getStatus());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public static AdminDto toAdminDto(Admin admin) {
    	if(admin==null) {
    		return null;
    	}
    	
        AdminDto adminDto = new AdminDto();
        adminDto.setId(admin.getId());
        adminDto.setUsername(admin.getUsername());
        adminDto.setEmail(admin.getEmail());
        adminDto.setStatus(admin.getStatus());
        adminDto.setRole(admin.getRole());
        adminDto.setQualification(admin.getQualification());
        return adminDto;
    }
}


