package com.modfathers.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modfathers.exception.DataNotFoundException;
import com.modfathers.model.Address;
import com.modfathers.model.User;
import com.modfathers.repository.AddressRepository;
import com.modfathers.repository.UserRepository;

@Service
public class AddressService {
	private final AddressRepository addressRepo;
	private final UserRepository userRepo;

	@Autowired
	public AddressService(AddressRepository addressRepo, UserRepository userRepo) {
		this.addressRepo = addressRepo;
		this.userRepo = userRepo;
	}
	
	public Address add(int id, Address address) {
		Address addr = userRepo.findById(id).map(user -> {
			address.setUser(user);
			return addressRepo.save(address); 
		}).orElseThrow(() -> new DataNotFoundException("Did not find user with id: " + id));
		return addr;
	}

	public Address findById(int id) {
		if (id <= 0) {
			return null;
		}
		return addressRepo.findById(id).orElse(null);
	}
	
	public List<Address> findByUserId(int user_id) {
		if (user_id <= 0) {
			return Collections.emptyList();
		}	
		return addressRepo.findByUserId(user_id);
	}
	
	public Address update(Address address) {
		Address matchAddress = addressRepo.findById(address.getId()).orElse(null);
		if (matchAddress != null) {
			if (address.getStreet() != null) {
				matchAddress.setStreet(address.getStreet());
			}
			if (address.getCity() != null) {
				matchAddress.setCity(address.getCity());				
			}
			if (address.getState() != null) {
				matchAddress.setState(address.getState());				
			}
			if (address.getZip() != null) {
				matchAddress.setZip(address.getZip());				
			}
			return addressRepo.save(matchAddress);
		}
		
		return null;
	}
	
	public boolean delete(int id) {
		if (addressRepo.existsById(id)) {
			addressRepo.deleteById(id);
			return true;
		}
		return false;
	}
}
