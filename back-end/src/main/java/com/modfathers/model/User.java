package com.modfathers.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Length(min=2)
	@Column(name = "first_name")
	private String firstName;
	@Length(min = 2)
	@Column(name = "last_name")
	private String lastName;
	
	@Length(min = 5)
	@Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]*")
	@Column(name = "username", unique=true)
	private String userName;
	
	@NotBlank
	private String password;
	
	@Email
	@Column(unique=true)
	private String email;
	
	@Length(min = 10, max = 10)
	@Pattern(regexp = "[0-9]*")
	private String phone;
	
	@Column(name = "reg_date")
	private LocalDateTime registrationDate;
	
	@Column(name = "cart")
	@ManyToMany
	@JoinColumn(name = "product_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Product> shoppingCart;
}
