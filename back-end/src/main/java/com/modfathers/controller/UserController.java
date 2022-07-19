package com.modfathers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modfathers.model.User;
import com.modfathers.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userServ;
	
	@PostMapping("/register")
	public User registerUser(@RequestBody User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		// encode password by hashing
		String encoded = encoder.encode(user.getPassword());
		return userServ.registerUser(user.getUserName(), encoded, user.getFirstName(), user.getLastName(), user.getPhone(), user.getEmail());
	}
}
