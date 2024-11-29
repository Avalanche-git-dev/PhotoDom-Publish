//package com.app.userservice.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//	
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if ("admin".equals(username)) {
//            return User.builder()
//                .username("sysadmin")
//                .password(passwordEncoder.encode("sysadminpw")) // Sostituisci con credenziali sicure
//                .roles("ADMIN")
//                .build();
//        }
//        throw new UsernameNotFoundException("User not found");
//    }
//}
