package com.modfathers.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modfathers.exception.DataNotFoundException;
import com.modfathers.model.Address;
import com.modfathers.model.CreditCard;
import com.modfathers.model.Payment;
import com.modfathers.repository.CreditCardRepository;
import com.modfathers.repository.PaymentRepository;

@Service
public class PaymentService {
	
	private PaymentRepository payRepo;
	private CreditCardRepository cardRepo;
	
	@Autowired
	public PaymentService(PaymentRepository payRepo, CreditCardRepository cardRepo) {
		this.payRepo = payRepo;
		this.cardRepo = cardRepo;
	}

	public Payment add(int id, Payment payment) {
		payment.setTimestamp(LocalDateTime.now());
		Payment pay = cardRepo.findById(id).map(card -> {
			payment.setCard(card);
			return payRepo.save(payment); 
		}).orElseThrow(() -> new DataNotFoundException("Did not credit card with id: " + id));
		return pay;
	}
	
	public Payment findById(int id) {
		if (id <= 0) {
			return null;
		}
		return payRepo.findById(id).orElse(null);
	}
	
	public Payment update(Payment payment) {
		Payment matchPayment = payRepo.findById(payment.getId()).orElse(null);
		if (matchPayment != null) {
			if (payment.getAmount() != 0) {
				matchPayment.setAmount(payment.getAmount());
			}
			if (payment.getState() != null) {
				matchPayment.setState(payment.getState());
			}
			return payRepo.save(matchPayment);
		}
		return null;
	}
	
	public boolean delete(int id) {
		if (payRepo.existsById(id)) {
			payRepo.deleteById(id);
			return true;
		} 
		return false;
	}
}
