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
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setBirthday(user.getBirthday());
        userDto.setAge(Long.valueOf(user.getAge()));
        userDto.setNickname(user.getNickname());
        userDto.setTelephone(user.getTelephone());
        userDto.setStatus(user.getStatus());
        userDto.setRole(user.getRole());
        userDto.setPhotoProfileId(user.getPhotoProfileId());
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
        adminDto.setFirstName(admin.getFirstName());
        adminDto.setLastName(admin.getLastName());
        adminDto.setNickname(admin.getNickname());
        adminDto.setTelephone(admin.getTelephone());
        adminDto.setBirthday(admin.getBirthday());
        adminDto.setAge(Long.valueOf(admin.getAge()));
        adminDto.setStatus(admin.getStatus());
        adminDto.setRole(admin.getRole());
        adminDto.setQualification(admin.getQualification());
        adminDto.setPhotoProfileId(admin.getPhotoProfileId());
        return adminDto;
    }
    
    
    public static ProfileView toProfileView(User user) {
        if (user == null) {
            return null;
        }
        return new ProfileView(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getBirthday(),
            Long.valueOf(user.getAge()), // Conversione esplicita in Long
            user.getNickname(),
            user.getTelephone(),
            user.getPhotoProfileId()
        );
    }
}


