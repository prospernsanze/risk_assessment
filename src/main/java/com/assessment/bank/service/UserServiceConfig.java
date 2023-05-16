package com.assessment.bank.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.assessment.bank.entities.User;
import com.assessment.bank.repositories.UserRepository;
import com.assessment.bank.config.UserInfoDetails ;


@Component
public class UserServiceConfig implements UserDetailsService {
	
	   @Autowired
	    private UserRepository repository;

	  
		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			Optional<User> userInfo = repository.findByEmail(username);
	        return userInfo.map(UserInfoDetails::new)
	                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

		}
}
