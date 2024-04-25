package com.microservices.authenticationservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.microservices.authenticationservice.entities.User;
import com.microservices.authenticationservice.repository.UserRepository;

@Service
public class UserService {

	private List<User> users = new ArrayList<>();
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public List<User> getUsers(){
		return userRepository.findAll();
	}
	
	public User createUser(User user) {
		user.setId(UUID.randomUUID().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
}
