package com.modfathers.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "credit_card")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
    private User user;
	
    @Column(nullable=false)
    @Length(min = 16, max = 16)
    String number;

    @Column(nullable=false)
    private String type;

    @Length(min=2)
    @Column(name = "holder_fname", nullable=false)
    private String holderFirstName;

    @Length(min = 2)
    @Column(name = "holder_lname", nullable=false)
    private String holderLastName;

    @Range(min = 1, max = 12)
    @Column(name = "exp_month", nullable=false)
    private int expMonth;

    @Column(name = "exp_year", nullable=false)
    private int expYear;

	public CreditCard(User user, @Length(min = 16, max = 16) String number, String type,
			@Length(min = 2) String holderFirstName, @Length(min = 2) String holderLastName,
			@Range(min = 1, max = 12) int expMonth, int expYear) {
		super();
		this.user = user;
		this.number = number;
		this.type = type;
		this.holderFirstName = holderFirstName;
		this.holderLastName = holderLastName;
		this.expMonth = expMonth;
		this.expYear = expYear;
	} 
}
