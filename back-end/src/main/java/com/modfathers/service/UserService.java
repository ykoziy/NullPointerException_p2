package com.modfathers.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modfathers.model.User;
import com.modfathers.repository.UserRepository;

@Service
public class UserService {
	
	private Logger log = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepo;
	
	@Autowired
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	public User registerUser(String userName, String password, String firstName, String lastName, String phone, String email) {
		User user = new User(0, firstName, lastName, userName, password, email, phone, LocalDate.now());
		return userRepo.save(user);
	}

}
