package com.modfathers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.modfathers.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
}