package com.modfathers.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.modfathers.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="orders")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private User user;
	
	@ManyToMany(cascade = CascadeType.REMOVE)
	private List<Product> products;
	
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private Payment payment;
	
	@Column(nullable = false)
	private double amount;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@Column(name = "order_date", nullable = false)
	private LocalDateTime orderDate;
	
	@Column(name = "update_date", nullable = false)
	private LocalDateTime updateDate;

	public Order(User user, double amount, OrderStatus status, LocalDateTime orderDate, LocalDateTime updateDate) {
		super();
		this.user = user;
		this.amount = amount;
		this.status = status;
		this.orderDate = orderDate;
		this.updateDate = updateDate;
	}

	public Order(double amount, OrderStatus status, LocalDateTime orderDate, LocalDateTime updateDate) {
		super();
		this.amount = amount;
		this.status = status;
		this.orderDate = orderDate;
		this.updateDate = updateDate;
	}

	public Order(User user, Payment payment, double amount, OrderStatus status) {
		super();
		this.user = user;
		this.payment = payment;
		this.amount = amount;
		this.status = status;
	}

	public Order(User user, Payment payment, double amount, OrderStatus status, LocalDateTime updateDate) {
		super();
		this.user = user;
		this.payment = payment;
		this.amount = amount;
		this.status = status;
		this.updateDate = updateDate;
	}

	public Order(User user, Payment payment, double amount) {
		super();
		this.user = user;
		this.payment = payment;
		this.amount = amount;
	}
}
