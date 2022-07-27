package com.modfathers.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name = "card_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
    private CreditCard card;
	
	@Column(nullable = false)
	private double amount;
	
	@Column(nullable = false)
	private String state;
	
	@Column(nullable = false)
	private LocalDateTime timestamp;

	public Payment(CreditCard card, double amount, String state) {
		super();
		this.card = card;
		this.amount = amount;
		this.state = state;
	}

	public Payment(CreditCard card, double amount, String state, LocalDateTime timestamp) {
		super();
		this.card = card;
		this.amount = amount;
		this.state = state;
		this.timestamp = timestamp;
	}
}
