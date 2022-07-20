package com.modfathers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modfathers.model.CreditCard;
import com.modfathers.model.Payment;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
	List<Payment> findByPaymentId(int payment_id);
}
