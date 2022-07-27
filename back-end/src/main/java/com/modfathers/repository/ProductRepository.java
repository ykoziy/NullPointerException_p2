package com.modfathers.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modfathers.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>
{
	//public Product getProductById(int id);
	public Optional<Product> getProductByName(String name);
	public String getImageURLById(int id);
}
