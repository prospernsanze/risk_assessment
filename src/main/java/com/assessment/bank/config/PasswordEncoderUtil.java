package com.assessment.bank.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {
	
	 public static String encodePassword(String password) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        return passwordEncoder.encode(password);
	    }

	    public static boolean matches(String rawPassword, String encodedPassword) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        return passwordEncoder.matches(rawPassword, encodedPassword);
	    }

}
