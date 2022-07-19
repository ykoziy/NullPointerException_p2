package com.modfathers.service;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.modfathers.exception.UserAlreadyExistException;
import com.modfathers.exception.UserAuthenticationException;
import com.modfathers.model.User;
import com.modfathers.repository.UserRepository;

@Service
public class UserService {
	
	private Logger log = LoggerFactory.getLogger(UserService.class);
	
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(UserRepository userRepo) {
		this.userRepo = userRepo;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}
	
	public User registerUser(String userName, String password, String firstName, String lastName, String phone, String email) {
		User u = userRepo.findByEmail(email).orElse(null);
		if (u == null) {
			User user = new User(0, firstName, lastName, userName, password, email, phone, LocalDate.now());
			return userRepo.save(user);
		} else {
			throw new UserAlreadyExistException("User already exists with this email");
		}
	}
	
	public User loginUser(String username, String password) {
		User matchUser = userRepo.findByUserName(username).orElse(null);
		if (matchUser != null) {
			boolean isPasswordMatching = passwordEncoder.matches(password, matchUser.getPassword());
			if(isPasswordMatching) {
				return matchUser;
			} else {
				throw new UserAuthenticationException("Wrong password for username: " + username);
			}
		} else {
			throw new UserAuthenticationException("Can't find user with such username: " + username);
		}
	}
	
	public Set<User> getUsers() {
		return userRepo.findAll().stream().collect(Collectors.toSet());
	}
	
	public User getByUsername(String username) {
		return userRepo.findByEmail(username).orElse(new User());
	}
	
	public User getById(int id) {
		return userRepo.getReferenceById(id);
	}
	
	public User getByEmail(String email) {
		return userRepo.findByEmail(email).orElse(new User());
	}
}
