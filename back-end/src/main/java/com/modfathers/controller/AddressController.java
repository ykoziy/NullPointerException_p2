package com.modfathers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modfathers.model.Address;
import com.modfathers.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private AddressService addressServ;
	
	
	@PostMapping("/add/{user_id}")
	public Address addAddress(@PathVariable("user_id") int userId, @RequestBody Address addr) {
		return addressServ.add(userId, addr);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Address> getById(@PathVariable("id") int id) {
		Address addr = addressServ.findById(id);
		if (addr == null) {
			return new ResponseEntity<Address>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Address>(addr, HttpStatus.OK);
		}
	}
	
	@GetMapping("/uid/{userId}")
	public ResponseEntity<List<Address>> getByUserId(@PathVariable("userId") int userId) {
		List<Address> addrList = addressServ.findByUserId(userId);
		if (addrList.isEmpty()) {
			return new ResponseEntity<List<Address>>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<Address>>(addrList, HttpStatus.OK);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Address> updateAddress(@RequestBody Address address) {
		Address addr = addressServ.update(address);
		if (addr == null) {
			return new ResponseEntity<Address>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Address>(addr, HttpStatus.OK);
		}		
	}
	
	@DeleteMapping("/delete/{id}")
	public boolean deleteUser(@PathVariable("id") int id) {
		return addressServ.delete(id);
	}
}
