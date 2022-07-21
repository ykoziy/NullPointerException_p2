package com.modfathers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modfathers.model.CreditCard;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
	List<CreditCard> findByUserId(int user_id);
}
