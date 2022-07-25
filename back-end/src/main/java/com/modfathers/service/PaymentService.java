package com.modfathers.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modfathers.exception.DataNotFoundException;
import com.modfathers.model.Address;
import com.modfathers.model.CreditCard;
import com.modfathers.model.Order;
import com.modfathers.model.Payment;
import com.modfathers.repository.CreditCardRepository;
import com.modfathers.repository.OrderRepository;
import com.modfathers.repository.PaymentRepository;

@Service
public class PaymentService {
	
	private PaymentRepository payRepo;
	private CreditCardRepository cardRepo;
	private OrderRepository ordeRepo;
	
	@Autowired
	public PaymentService(PaymentRepository payRepo, CreditCardRepository cardRepo, OrderRepository ordeRepo) {
		super();
		this.payRepo = payRepo;
		this.cardRepo = cardRepo;
		this.ordeRepo = ordeRepo;
	}
	
	public Payment add(int card_id, Payment payment) {
		payment.setTimestamp(LocalDateTime.now());
		Payment pay = cardRepo.findById(card_id).map(card -> {
			payment.setCard(card);
			return payRepo.save(payment); 
		}).orElseThrow(() -> new DataNotFoundException("Did not find credit card with id: " + card_id));
		return pay;
	}
	
	public Payment add(int card_id, int order_id, Payment payment) {
		CreditCard card = cardRepo.findById(card_id).orElse(null);
		if (card != null) {
			Order order = ordeRepo.findById(order_id).orElse(null);
			if (order != null) {
				payment.setTimestamp(LocalDateTime.now());
				payment.setCard(card);
				payment.setOrder(order);
				return payRepo.save(payment);
			} else {
				throw new DataNotFoundException("Did not find order with id: " + order_id);			
			}
		} else {
			throw new DataNotFoundException("Did not find credit card with id: " + card_id);
		}
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
