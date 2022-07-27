package com.modfathers.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modfathers.model.Product;
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
		return userServ.registerUser(user.getUserName(), encoded, user.getFirstName(), user.getLastName(), user.getPhone(), user.getEmail(), user.getShoppingCart());
	}
	
	@PostMapping("/login")
	public User loginUser(@RequestBody Map<String, String> requestBody) {
		String username = requestBody.get("userName");
		String password = requestBody.get("password");
		return userServ.loginUser(username, password);
	}
	
	@GetMapping
	public Set<User> getAll() {
		return userServ.getUsers();
	}
	
	@GetMapping("/uname")
	public ResponseEntity<User> getByUsername(@RequestHeader("username") String username) {
		User u = userServ.getByUsername(username);
		if (u == null) {
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<User>(u, HttpStatus.OK);
		}
	}
	
	@GetMapping("/email")
	public ResponseEntity<User> getByEmail(@RequestHeader("email") String email) {
		User u = userServ.getByEmail(email);
		if (u == null) {
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<User>(u, HttpStatus.OK);
		}
	}
	
	@GetMapping("/id")
	public ResponseEntity<User> getById(@RequestHeader("id") int id) {
		User u = userServ.findById(id);
		if (u == null) {
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<User>(u, HttpStatus.OK);
		}
	}
	
	@PutMapping("/update")
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		User u = userServ.updateUser(user);
		if (u == null) {
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<User>(u, HttpStatus.OK);
		}		
	}
	
	@DeleteMapping("/delete/{id}")
	public boolean deleteUser(@PathVariable("id") int id) {
		return userServ.delete(id);
	}
}
