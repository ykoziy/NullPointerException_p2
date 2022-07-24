package com.modfathers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.modfathers.exception.DataNotFoundException;
import com.modfathers.model.Address;
import com.modfathers.model.CreditCard;
import com.modfathers.model.User;
import com.modfathers.repository.AddressRepository;
import com.modfathers.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
	
	@Mock
	private AddressRepository addressRepo;
	
	@Mock
	private UserRepository userRepo;
	
	private AddressService addressServ;
	
	private Address testAddress;
	private Address testAddress2;
	private User testUser;
	
	@BeforeEach
	void beforeEach() {
		addressServ = new AddressService(addressRepo, userRepo);
	}
	
	@AfterEach
	void cleanUp() {
		testAddress = null;
		testAddress2 = null;
		testUser = null;
	}
	
	@Test
	void shouldBeAbleToAddAddressForUser() {
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
		
		testAddress = new Address( 1, null, "503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		
		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		when(addressRepo.save(testAddress)).thenReturn(testAddress);
		
		Address test = addressServ.add(12, testAddress);
		
		assertEquals(testUser, test.getUser());
		assertEquals(testAddress.getStreet(), test.getStreet());
	}
	
	@Test
	void shouldNotAddAddressIfNoUserFound() {
		testAddress = new Address();
		when(userRepo.findById(12)).thenReturn(Optional.empty());
		assertThrows(DataNotFoundException.class, () -> {
			addressServ.add(12, testAddress);
		});
	}
	
	@Test
	void shouldNotDeleteIfNoAddressFound() {
		assertFalse(addressServ.delete(100));
	}
	
	@Test
	void shouldDeleteAddress() {
		when(addressRepo.existsById(100)).thenReturn(true);
		assertTrue(addressServ.delete(100));
	}
	
	@Test
	void shouldGetAddressById() {
		testAddress = new Address( "503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		when(addressRepo.findById(1)).thenReturn(Optional.of(testAddress));
		Address a = addressServ.findById(1);
		assertEquals(testAddress, a);
	}
	
	@Test
	void shouldNotGetAddressForInvalidId() {
		Address a = addressServ.findById(0);
		assertNull(a);
	}
	
	@Test
	void shouldGetAddressesByUserId() {
		List<Address> listAddress = new LinkedList<>();
		testAddress = new Address( "503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		testAddress2 = new Address( "503 S Humbolt Ave #19", "Ellinwood", "KS", "67526");
		listAddress.add(testAddress);
		listAddress.add(testAddress2);
		when(addressRepo.findByUserId(1)).thenReturn(listAddress);
		
		List<Address> resultList = addressServ.findByUserId(1);
		assertEquals(2, resultList.size());
		assertEquals("503 S Humbolt Ave #19", resultList.get(1).getStreet());
	}
	
	@Test
	void shouldReturnEmptyListIfInvalidUserId() {
		List<Address> resultList = addressServ.findByUserId(0);
		assertEquals(0, resultList.size());
	}
	
	@Test
	void shouldNotUpdateIfCreditCardNotFound() {
		testAddress = new Address();
		testAddress.setId(1);
		when(addressRepo.findById(1)).thenReturn(Optional.empty());
		Address a = addressServ.update(testAddress);
		assertNull(a);
	}
	
	@Test
	void shouldUpdateUser() {
		testAddress = new Address("503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		testAddress2 = new Address( "598 S Humbolt Ave #13", "Butler", "PA", "12345");
		testAddress2.setId(1);
		when(addressRepo.findById(1)).thenReturn(Optional.of(testAddress));
		when(addressRepo.save(any(Address.class))).thenReturn(testAddress2);
		Address result  = addressServ.update(testAddress2);
		assertNotEquals(testAddress, result);
	}
	
	@Test
	void shouldNotUpdateIfNewDataEmpty() {
		testAddress = new Address("503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");

		testAddress2 = new Address();
		testAddress2.setId(1);
		
		when(addressRepo.findById(1)).thenReturn(Optional.of(testAddress));
		addressServ.update(testAddress2);
		assertEquals("503 S Humbolt Ave #13", testAddress.getStreet());		
	}
}
