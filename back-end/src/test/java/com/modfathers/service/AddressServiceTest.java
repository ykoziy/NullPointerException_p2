package com.modfathers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.modfathers.repository.AddressRepository;
import com.modfathers.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
	
	@Mock
	private AddressRepository addressRepo;
	
	@Mock
	private UserRepository userRepo;
	
	private AddressService addressServ;
	
	@BeforeEach
	void beforeEach() {
		addressServ = new AddressService(addressRepo, userRepo);
	}
	
	@Test
	void shouldBeAbleToAddAddressForUser() {
		
	}
	
	@Test
	void shouldGetAddressById() {

	}
	
	@Test
	void shouldGetAddressesByUserId() {

	}
	
	@Test
	void shouldNotUpdateUserIfNothingChanges() {
		
	}
	
	@Test
	void shouldOnlyUpdateStreet() {
	
	}
	
	@Test
	void shouldOnlyUpdateCity() {
	
	}
	
	@Test
	void shouldOnlyUpdateState() {
	
	}
	
	@Test
	void shouldOnlyUpdateZip() {
	
	}
}
