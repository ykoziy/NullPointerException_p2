package com.modfathers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modfathers.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
