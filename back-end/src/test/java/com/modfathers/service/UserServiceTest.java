package com.modfathers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.modfathers.exception.UserAlreadyExistException;
import com.modfathers.exception.UserAuthenticationException;
import com.modfathers.model.User;
import com.modfathers.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private UserRepository userRepo;

	private UserService userService;

	@BeforeEach
	void beforeEach() {
		userService = new UserService(userRepo);
	}

	@Test
	void newUserIsCreatedWithValidDate() {
		User testUser = new User(12, "Bob", "Smith", "testUser", "password", "bob@example.com", "4206669999",
				LocalDateTime.now());
		when(userRepo.save(any(User.class))).thenReturn(testUser);
		User u = userService.registerUser("testUser", "password", "Bob", "Smith", "4206669999", "bob@example.com");
		assertEquals(testUser, u);
	}

	@Test
	void cantRegistreIfEmailExists() {
		User testUser = new User(12, "Bob", "Smith", "testUser", "password", "bob@example.com", "4206669999",
				LocalDateTime.now());
		when(userRepo.save(any(User.class)))
				.thenThrow(new UserAlreadyExistException("User already exists with this email"));
		Throwable exception = assertThrows(UserAlreadyExistException.class, () -> {
			User u = userService.registerUser("testUser", "password", "Bob", "Smith", "4206669999", "bobs@example.com");
		});

		assertEquals("User already exists with this email", exception.getMessage());
	}

	@Test
	void userCanLogin() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode("password");
		User testUser = new User(12, "Bob", "Smith", "testUser", encoded, "bob@example.com", "4206669999",
				LocalDateTime.now());
		when(userRepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));
		User u = userService.loginUser("testUser", "password");
		assertEquals(testUser, u);
	}

	@Test
	void userCantLoginWrongPassword() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode("password");
		User testUser = new User(12, "Bob", "Smith", "testUser", encoded, "bob@example.com", "4206669999",
				LocalDateTime.now());
		when(userRepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));
		Throwable exception = assertThrows(UserAuthenticationException.class, () -> {
			User u = userService.loginUser("testUser", "123");
		});
		assertEquals("Wrong password for username: testUser", exception.getMessage());
	}

	@Test
	void userCantLoginNoSuchUsername() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode("password");
		User testUser = new User(12, "Bob", "Smith", "testUser", encoded, "bob@example.com", "4206669999",
				LocalDateTime.now());
		when(userRepo.findByUserName(anyString())).thenReturn(Optional.empty());
		Throwable exception = assertThrows(UserAuthenticationException.class, () -> {
			User u = userService.loginUser("testUser2", "password");
		});
		assertEquals("Can't find user with such username: testUser2", exception.getMessage());
	}
}
