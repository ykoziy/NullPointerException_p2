package com.modfathers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.modfathers.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer>
{
	//public Product getProductById(int id);
	public Optional<Product> getProductByName(String name);
	public List<Product> getByCategory(String name);
	public String getImageURLById(int id);
}