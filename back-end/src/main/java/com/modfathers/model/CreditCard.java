package com.modfathers.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

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
	
    @Column(nullable=false)
    @Range(min = 16, max = 16)
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
}
