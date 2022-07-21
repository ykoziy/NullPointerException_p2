package com.modfathers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.modfathers.model.Address;
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
	void shouldGetAddressById() {
		testAddress = new Address( "503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		when(addressRepo.findById(1)).thenReturn(Optional.of(testAddress));
		Address a = addressServ.findById(1);
		assertEquals(testAddress, a);
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
	void shouldNotUpdateUserIfNothingChanges() {
		testAddress = new Address( "503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		testAddress2 = new Address( "503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		Address newAddress = new Address();
		newAddress.setId(1);
		when(addressRepo.findById(1)).thenReturn(Optional.of(testAddress2));
		addressServ.update(newAddress);
		assertEquals(testAddress, testAddress2);
	}
	
	@Test
	void shouldOnlyUpdateStreet() {
		testAddress = new Address( "503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		Address newAddress = new Address();
		newAddress.setId(1);
		newAddress.setStreet("100 example st.");
		when(addressRepo.findById(1)).thenReturn(Optional.of(testAddress));
		addressServ.update(newAddress);
		
		assertEquals("100 example st.", testAddress.getStreet());
		assertNotNull(testAddress.getCity());
		assertNotNull(testAddress.getState());
		assertNotNull(testAddress.getZip());
		
	}
	
	@Test
	void shouldOnlyUpdateCity() {
		testAddress = new Address( "503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		Address newAddress = new Address();
		newAddress.setId(1);
		newAddress.setCity("new york");
		when(addressRepo.findById(1)).thenReturn(Optional.of(testAddress));
		addressServ.update(newAddress);
		
		assertEquals("new york", testAddress.getCity());
		assertNotNull(testAddress.getStreet());
		assertNotNull(testAddress.getState());
		assertNotNull(testAddress.getZip());	
	}
	
	@Test
	void shouldOnlyUpdateState() {
		testAddress = new Address( "503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		Address newAddress = new Address();
		newAddress.setId(1);
		newAddress.setState("NY");
		when(addressRepo.findById(1)).thenReturn(Optional.of(testAddress));
		addressServ.update(newAddress);
		
		assertEquals("NY", testAddress.getState());
		assertNotNull(testAddress.getCity());
		assertNotNull(testAddress.getStreet());
		assertNotNull(testAddress.getZip());
	}
	
	@Test
	void shouldOnlyUpdateZip() {
		testAddress = new Address( "503 S Humbolt Ave #13", "Ellinwood", "KS", "67526");
		Address newAddress = new Address();
		newAddress.setId(1);
		newAddress.setZip("12345");
		when(addressRepo.findById(1)).thenReturn(Optional.of(testAddress));
		addressServ.update(newAddress);
		
		assertEquals("12345", testAddress.getZip());
		assertNotNull(testAddress.getCity());
		assertNotNull(testAddress.getState());
		assertNotNull(testAddress.getState());
	}
}
