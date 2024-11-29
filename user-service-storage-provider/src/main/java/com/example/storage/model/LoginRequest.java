package com.example.storage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginRequest {
    private String username;
    private String password;
    

}
