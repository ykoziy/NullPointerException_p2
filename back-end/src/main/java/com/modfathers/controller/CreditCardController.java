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

import com.modfathers.model.CreditCard;
import com.modfathers.service.CreditCardService;

@RestController
@RequestMapping("/cards")
public class CreditCardController {
	
	@Autowired
	private CreditCardService cardServ;
	
	@PostMapping("/add")
	public CreditCard addPayment(@RequestBody CreditCard newCard) {
		return cardServ.add(newCard);
	}
	
	@GetMapping("/id")
	public ResponseEntity<CreditCard> getById(@RequestHeader("id") int id) {
		CreditCard card = cardServ.findById(id);
		if (card == null) {
			return new ResponseEntity<CreditCard>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<CreditCard>(card, HttpStatus.OK);
		}		
	}
	
	@PutMapping("/update")
	public ResponseEntity<CreditCard> updateCreditCard(@RequestBody CreditCard card) {
		CreditCard c = cardServ.update(card);
		if (c == null) {
			return new ResponseEntity<CreditCard>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<CreditCard>(c, HttpStatus.OK);
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public boolean deleteCreditCard(@PathVariable("id") int id) {
		return cardServ.delete(id);
	}
}