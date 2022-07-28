package com.modfathers.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.modfathers.model.Product;
import com.modfathers.service.ProductService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product")
public class ProductController
{

	@Autowired
	private ProductService productServ;
	
	@PostMapping("/add")
	public Product addProduct(@RequestBody Product product)
	{
		return productServ.add(product);
	}
	
	@GetMapping
	public List<Product> getAll()
	{
		return productServ.findAll();
	}
	
	@GetMapping("/id")
	public ResponseEntity<Product> getProductById(@RequestHeader("id") int id)
	{
		Product temp = productServ.FindProductById(id);
		if(Objects.isNull(temp))
		{
			return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Product>(temp, HttpStatus.OK);
		
	}
	
	@GetMapping("/name")
	public ResponseEntity<Product> getProductByName(@RequestHeader("name") String name)
	{
		Product temp = productServ.FindProductByName(name);
		if(Objects.isNull(temp))
		{
			return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Product>(temp, HttpStatus.OK);
		
	}
	
	@PutMapping("/update")
	public ResponseEntity<Product> updateProduct(@RequestBody Product product)
	{
		Product temp = productServ.update(product);
		if(Objects.isNull(temp))
		{
			return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Product>(temp, HttpStatus.OK);
		
	}
	
	@DeleteMapping("/delete")
	public boolean deleteProduct(@RequestHeader("id") int id)
	{
		return productServ.delete(id);
	}
	
	@GetMapping("/category/{category}")
	public ResponseEntity<List<Product>> getAllByCategoryName(@PathVariable("category") String category) {
		List<Product> prodList = productServ.getAllByCategoryName(category);
		System.out.println(prodList);
		if (prodList.isEmpty()) {
			return new ResponseEntity<List<Product>>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<Product>>(prodList, HttpStatus.OK);
		}
	}
}
