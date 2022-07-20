package com.modfathers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.modfathers.model.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

}
