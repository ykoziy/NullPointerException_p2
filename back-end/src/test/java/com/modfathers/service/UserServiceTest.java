package com.modfathers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
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

	private User testUser2;

	private User testUser;
	private User newUser;

	@BeforeEach
	void beforeEach() {
		userService = new UserService(userRepo);
	}

	@AfterEach
	void cleanUp() {
		testUser2 = null;
		testUser = null;
		newUser = null;
	}

	@Test
	void newUserIsCreatedWithValidDate() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("password");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		
		when(userRepo.save(any(User.class))).thenReturn(testUser);
		
		User u = userService.registerUser("testUser", "password", "Bob", "Smith", "4206669999", "bob@example.com");
		assertEquals(testUser, u);
	}

	@Test
	void cantRegistreIfUsernameExists() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("password");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		
		when(userRepo.findByUserName("testUser")).thenReturn(Optional.of(testUser));
		assertThrows(UserAlreadyExistException.class, () -> {
			userService.registerUser("testUser", "password", "Bob", "Smith", "4206669999", "bob@example.com");
		});
	}
	
	@Test
	void cantRegistreIfEmailExists() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("password");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		
		when(userRepo.findByEmail("bob@example.com")).thenReturn(Optional.of(testUser));
		assertThrows(UserAlreadyExistException.class, () -> {
			userService.registerUser("testUser", "password", "Bob", "Smith", "4206669999", "bob@example.com");
		});
	}

	@Test
	void userCanLogin() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode("password");
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword(encoded);
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		
		when(userRepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));
		
		User u = userService.loginUser("testUser", "password");
		assertEquals(testUser, u);
	}

	@Test
	void userCantLoginWrongPassword() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode("password");
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword(encoded);
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		
		when(userRepo.findByUserName(anyString())).thenReturn(Optional.of(testUser));
		Throwable exception = assertThrows(UserAuthenticationException.class, () -> {
			userService.loginUser("testUser", "123");
		});
		assertEquals("Wrong password for username: testUser", exception.getMessage());
	}

	@Test
	void userCantLoginNoSuchUsername() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode("password");
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword(encoded);
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		
		when(userRepo.findByUserName(anyString())).thenReturn(Optional.empty());
		
		Throwable exception = assertThrows(UserAuthenticationException.class, () -> {
			userService.loginUser("testUser2", "password");
		});
		assertEquals("Can't find user with such username: testUser2", exception.getMessage());
	}

	@Test
	void shouldNotUpdateIfUserNotFound() {
		testUser = new User();
		testUser.setId(1);
		when(userRepo.findById(1)).thenReturn(Optional.empty());
		User u = userService.updateUser(testUser);
		assertNull(u);
	}
	
	@Test
	void shouldNotUpdateUserIfNothingChanges() {
		LocalDateTime time = LocalDateTime.now();
		testUser2 = new User(12, "Bob", "Smith", "testUser", "pwd", "bob@example.com", "4206669999", time);

		testUser = new User(12, "Bob", "Smith", "testUser", "pwd", "bob@example.com", "4206669999", time);
		newUser = new User(12, null, null, null, null, null, null, null);
		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		userService.updateUser(newUser);
		assertEquals(testUser, testUser2);
	}

	@Test
	void shouldOnlyUpdateFirstName() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("pwd");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		
		newUser = new User();
		newUser.setId(12);
		newUser.setFirstName("Mike");
		
		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		userService.updateUser(newUser);
		assertEquals("Mike", testUser.getFirstName());
		assertNotNull(testUser.getLastName());
		assertNotNull(testUser.getEmail());
		assertNotNull(testUser.getId());
		assertNotNull(testUser.getRegistrationDate());
		assertNotNull(testUser.getPhone());
		assertNotNull(testUser.getPassword());
		assertNotNull(testUser.getId());
	}

	@Test
	void shouldOnlyUpdateLastName() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("pwd");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		
		newUser = new User();
		newUser.setId(12);
		newUser.setLastName("Klein");
		
		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		userService.updateUser(newUser);
		
		assertEquals("Klein", testUser.getLastName());
		assertNotNull(testUser.getLastName());
		assertNotNull(testUser.getEmail());
		assertNotNull(testUser.getId());
		assertNotNull(testUser.getRegistrationDate());
		assertNotNull(testUser.getPhone());
		assertNotNull(testUser.getPassword());
		assertNotNull(testUser.getId());
	}

	@Test
	void shouldOnlyUpdatePassword() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("pwd");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		
		newUser = new User();
		newUser.setId(12);
		newUser.setPassword("newPassword");
		
		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		userService.updateUser(newUser);
		
		assertTrue(encoder.matches("newPassword", testUser.getPassword()));
		assertNotNull(testUser.getLastName());
		assertNotNull(testUser.getFirstName());
		assertNotNull(testUser.getEmail());
		assertNotNull(testUser.getId());
		assertNotNull(testUser.getRegistrationDate());
		assertNotNull(testUser.getPhone());
		assertNotNull(testUser.getId());
	}

	@Test
	void shouldOnlyUpdateEmail() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("pwd");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());

		newUser = new User();
		newUser.setId(12);
		newUser.setEmail("mimimi@example.com");
		
		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		userService.updateUser(newUser);
		
		assertEquals("mimimi@example.com", testUser.getEmail());
		assertNotNull(testUser.getFirstName());
		assertNotNull(testUser.getLastName());
		assertNotNull(testUser.getId());
		assertNotNull(testUser.getRegistrationDate());
		assertNotNull(testUser.getPhone());
		assertNotNull(testUser.getPassword());
		assertNotNull(testUser.getId());
	}
	
	@Test
	void shouldNotUpdateEmailIfAlreadyExists() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("pwd");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());

		newUser = new User();
		newUser.setId(12);
		newUser.setEmail("mimimi@example.com");
		
		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		when(userRepo.findByEmail("mimimi@example.com")).thenReturn(Optional.of(testUser));
		assertThrows(UserAlreadyExistException.class, () -> {
			userService.updateUser(newUser);
		});
	}
	
	@Test
	void shouldNotUpdateEmailIfEqualsToNewOne() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("pwd");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());

		newUser = new User();
		newUser.setId(12);
		newUser.setEmail("bob@example.com");
		
		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		when(userRepo.save(any(User.class))).thenReturn(testUser);
		
		User result  = userService.updateUser(newUser);
		assertEquals("bob@example.com", result.getEmail());
	}

	@Test
	void shouldOnlyUpdatePhone() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("pwd");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());

		newUser = new User();
		newUser.setId(12);
		newUser.setPhone("6666666666");

		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		userService.updateUser(newUser);

		assertEquals("6666666666", testUser.getPhone());
		assertNotNull(testUser.getFirstName());
		assertNotNull(testUser.getLastName());
		assertNotNull(testUser.getId());
		assertNotNull(testUser.getRegistrationDate());
		assertNotNull(testUser.getEmail());
		assertNotNull(testUser.getPassword());
		assertNotNull(testUser.getId());
	}

	@Test
	void shouldGetUserByUsername() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("pwd");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		when(userRepo.findByUserName("testUser")).thenReturn(Optional.of(testUser));
		User u = userService.getByUsername("testUser");
		assertEquals("bob@example.com", u.getEmail());
	}

	@Test
	void shouldGetUserByEmail() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("pwd");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		when(userRepo.findByEmail("bob@example.com")).thenReturn(Optional.of(testUser));
		User u = userService.getByEmail("bob@example.com");
		assertEquals("Smith", u.getLastName());
	}

	@Test
	void shouldGetUserById() {
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword("pwd");
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		User u = userService.findById(12);
		assertEquals("bob@example.com", u.getEmail());
	}
	
	@Test
	void ShouldGetAllUsers() {
		testUser2 = new User();
		testUser2.setUserName("user2");
		testUser = new User();
		testUser.setUserName("user");
		newUser = new User();
		testUser2.setUserName("newUser");
		
		List<User> uList = new LinkedList<User>();
		
		uList.add(testUser2);
		uList.add(testUser);
		uList.add(newUser);
		
		when(userRepo.findAll()).thenReturn(uList);
		Set<User> resultSet = userService.getUsers();
		assertEquals(3, resultSet.size());
	}
	
	@Test
	void shouldNotDeleteIfNouserFound() {
		assertFalse(userService.delete(100));
	}
	
	@Test
	void shouldDeleteUser() {
		when(userRepo.existsById(100)).thenReturn(true);
		assertTrue(userService.delete(100));
	}
	
	@Test
	void shouldNotGetUserForInvalidId() {
		User u = userService.findById(0);
		assertNull(u);
	}
}
