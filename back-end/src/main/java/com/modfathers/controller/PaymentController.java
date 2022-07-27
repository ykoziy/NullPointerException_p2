package com.modfathers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.modfathers.model.Payment;
import com.modfathers.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {
	
	@Autowired
	private PaymentService payServ;
	
	@PostMapping("/add/{card_id}")
	public Payment addPayment(@PathVariable("card_id") int cardId, @RequestBody Payment payment) {
		return payServ.add(cardId, payment);
	}
	
	@PostMapping("/add")
	public Payment addPayment(@RequestHeader("card_id") int cardId, @RequestHeader("order_id") int orderId, @RequestBody Payment payment) {
		return payServ.add(cardId, orderId, payment);
	}
	
	@GetMapping("/id/{id}")
	public Payment getById(@PathVariable("id") int id) {
		return payServ.findById(id);
	}
	
	@DeleteMapping("/delete/{id}")
	public boolean deletePayment(@PathVariable("id") int id) {
		return payServ.delete(id);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Payment> updatePayment(@RequestBody Payment payment) {
		Payment p = payServ.update(payment);
		if (p == null) {
			return new ResponseEntity<Payment>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<Payment>(p, HttpStatus.OK);
		}
	}
}
