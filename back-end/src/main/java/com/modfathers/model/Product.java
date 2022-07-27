package com.modfathers.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product
{
	@Id
	@Column(name = "product_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "product_name", unique = true)
	private String name;
	
	@Column
	private String description;
	
	@Column
	private int inventory = 0;
	
	@Column(name = "image_ref")
	private String imageURL;

	public Product(String name, String description, int inventory, String imageURL) {
		super();
		this.name = name;
		this.description = description;
		this.inventory = inventory;
		this.imageURL = imageURL;
	}
}