package com.modfathers.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modfathers.exception.ProductAlreadyExists;
import com.modfathers.exception.ProductDoesNotExist;
import com.modfathers.model.Product;
import com.modfathers.repository.ProductRepository;

@Service
public class ProductService
{

	@Autowired
	ProductRepository productRepo;
	
	public List<Product> findAll()//
	{
		return productRepo.findAll();
	}
	
	public Product add(Product p)//
	{
		if(productRepo.existsById(p.getId()))
		{
			throw new ProductAlreadyExists("Product already exists with this id");
		}
		return productRepo.save(p);
	}
	
	public Product update(Product p)
	{
		if(!productRepo.existsById(p.getId()))
		{
			throw new ProductDoesNotExist("cannot update a product that does not already exist");
		}
		return productRepo.save(p);
	}
	
	public String getImageURL(int id)
	{
		String temp = productRepo.getImageURLById(id);
		if(temp!= null)
			return temp;
		else
			throw new ProductDoesNotExist("could not find product");
	}
	
	public Product FindProductById(int id)
	{
		Product temp =  productRepo.findById(id).orElse(null);
		if(Objects.isNull(temp))
		{
			throw new ProductDoesNotExist("could not find product");
		}
		return temp;
	}
	
	/*public Set<Product> getProducts()
	{
		return productRepo.findAll().stream().collect(Collectors.toSet());
	}*/
	
	public Product FindProductByName(String name)
	{
		Product temp =  productRepo.getProductByName(name).orElse(null);
		if(Objects.isNull(temp))
		{
			throw new ProductDoesNotExist("could not find product");
		}
		return temp;
	}
	
	public boolean delete(int id) {
		if (productRepo.existsById(id)) {
			productRepo.deleteById(id);
			return true;
		}
		return false;
	}
	
}
