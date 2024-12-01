package com.example.storage.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@ToString
@Data
public class UserDto {
	
	
    private Long id;
    private String username;
	private String email;
	private String firstName;
    private String lastName;
    private LocalDate birthday;
    private Long age;
    private String nickname;
    private String telephone;
	private String status;
    private String role;
    private String qualification;
    
    
    
    
}
