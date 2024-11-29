package com.example.storage.model;

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
    
	private String status;
    private String role;
    private String qualification;
    
    
    
    
}
