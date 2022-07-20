package com.modfathers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.modfathers.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
	List<Address> findByUserId(int user_id);
}
