package com.modfathers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.modfathers.model.CreditCard;
import com.modfathers.repository.CreditCardRepository;

@Service
public class CreditCardService {

	private final CreditCardRepository cardRepo;

	@Autowired
	public CreditCardService(CreditCardRepository cardRepo) {
		this.cardRepo = cardRepo;
	}

	public CreditCard add(CreditCard card) {
		return cardRepo.save(card);
	}

	public CreditCard findById(int id) {
		if (id <= 0) {
			return null;
		}
		return cardRepo.findById(id).orElse(null);
	}

	public CreditCard update(CreditCard newCard) {
		CreditCard matchCard = cardRepo.findById(newCard.getId()).orElse(null);
		if (matchCard != null) {
			if (newCard.getNumber() != null) {
				matchCard.setNumber(newCard.getNumber());
			}

			if (newCard.getType() == null) {
				matchCard.setType(newCard.getType());
			}

			if (newCard.getHolderFirstName() == null) {
				matchCard.setHolderFirstName(newCard.getHolderFirstName());
			}

			if (newCard.getHolderLastName() == null) {
				matchCard.setHolderLastName(newCard.getHolderLastName());
			}
			
			if (newCard.getExpMonth() != 0) {
				matchCard.setExpMonth(newCard.getExpMonth());
			}
			
			if (newCard.getExpYear() != 0) {
				matchCard.setExpYear(newCard.getExpYear());
			}

			return cardRepo.save(matchCard);
		}
		return null;
	}
	
	public boolean delete(int id) {
		if (cardRepo.existsById(id)) {
			cardRepo.deleteById(id);
			return true;
		} 
		return false;
	}
}
