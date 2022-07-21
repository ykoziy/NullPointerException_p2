package com.modfathers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.modfathers.model.CreditCard;

public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

}
