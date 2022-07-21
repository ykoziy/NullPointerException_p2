package com.modfathers.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.modfathers.exception.DataNotFoundException;
import com.modfathers.model.Address;
import com.modfathers.model.CreditCard;
import com.modfathers.model.User;
import com.modfathers.repository.CreditCardRepository;
import com.modfathers.repository.UserRepository;

@Service
public class CreditCardService {

	private final CreditCardRepository cardRepo;
	private final UserRepository userRepo;

	@Autowired
	public CreditCardService(CreditCardRepository cardRepo, UserRepository userRepo) {
		this.cardRepo = cardRepo;
		this.userRepo = userRepo;
	}

	public CreditCard add(CreditCard card) {
		return cardRepo.save(card);
	}
	
	public CreditCard add(int user_id, CreditCard card) {
		User u = userRepo.findById(user_id).orElse(null);
		if (u != null) {
			card.setUser(u);
			CreditCard newCard = cardRepo.save(card);
			return cardRepo.save(newCard);
		} else {
			throw new DataNotFoundException("Did not find user with id: " + user_id);
		}
	}
	
	public List<CreditCard> findByUserId(int user_id) {
		if (user_id <= 0) {
			return Collections.emptyList();
		}	
		return cardRepo.findByUserId(user_id);
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

			if (newCard.getType() != null) {
				matchCard.setType(newCard.getType());
			}

			if (newCard.getHolderFirstName() != null) {
				matchCard.setHolderFirstName(newCard.getHolderFirstName());
			}

			if (newCard.getHolderLastName() != null) {
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
