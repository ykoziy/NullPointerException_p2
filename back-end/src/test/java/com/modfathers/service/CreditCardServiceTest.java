package com.modfathers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.modfathers.exception.DataNotFoundException;
import com.modfathers.model.CreditCard;
import com.modfathers.model.User;
import com.modfathers.repository.CreditCardRepository;
import com.modfathers.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CreditCardServiceTest {

	@Mock
	private UserRepository userRepo;
	
	@Mock
	private CreditCardRepository creditRepo;
	
	private CreditCardService creditServ;
	
	private CreditCard testCard;
	private CreditCard testCard2;

	private User testUser;
	
	@BeforeEach
	void beforeEach() {
		creditServ = new CreditCardService(creditRepo, userRepo);
	}
	
	void afterEach() {
		testCard = null;
		testCard2 = null;
		testUser = null;
	}

	@Test
	void shouldBeAbleToAddCreditCard() {
		testCard = new CreditCard();
		testCard.setType("Visa");
		testCard.setHolderFirstName("Bob");
		testCard.setHolderLastName("Smith");
		testCard.setExpMonth(9);
		testCard.setExpYear(2022);
		testCard.setNumber("4069282136832346");
		
		when(creditRepo.save(any(CreditCard.class))).thenReturn(testCard);
		
		CreditCard result = creditServ.add(testCard);
		assertEquals(testCard, result);
	}
	
	@Test
	void shouldNotDeleteIfNoCreditCardFound() {
		assertFalse(creditServ.delete(100));
	}
	
	@Test
	void shouldDeleteCreditCard() {
		when(creditRepo.existsById(100)).thenReturn(true);
		assertTrue(creditServ.delete(100));
	}
	
	@Test
	void shouldBeAbleToAddCreditCardForUserId() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode("password");
		testUser = new User();
		testUser.setId(12);
		testUser.setFirstName("Bob");
		testUser.setLastName("Smith");
		testUser.setUserName("testUser");
		testUser.setPassword(encoded);
		testUser.setEmail("bob@example.com");
		testUser.setPhone("4206669999");
		testUser.setRegistrationDate(LocalDateTime.now());
		
		testCard = new CreditCard();
		testCard.setType("Visa");
		testCard.setHolderFirstName("Bob");
		testCard.setHolderLastName("Smith");
		testCard.setExpMonth(9);
		testCard.setExpYear(2022);
		testCard.setNumber("4069282136832346");
		
		when(userRepo.findById(12)).thenReturn(Optional.of(testUser));
		when(creditRepo.save(testCard)).thenReturn(testCard);
		CreditCard card = creditServ.add(12, testCard);
		
		assertEquals(testUser, card.getUser());
		assertEquals(testCard.getNumber(), card.getNumber());	
	}
	
	@Test
	void shouldNotAddCreditCardIfNoUserFound() {
		testCard = new CreditCard();
		when(userRepo.findById(12)).thenReturn(Optional.empty());
		assertThrows(DataNotFoundException.class, () -> {
			creditServ.add(12, testCard);
		});
	}
	
	@Test
	void shouldBeAbleToGetCreditCardsByUserId() {
		testCard = new CreditCard();
		testCard.setType("Visa");
		testCard.setHolderFirstName("Bob");
		testCard.setHolderLastName("Smith");
		testCard.setExpMonth(9);
		testCard.setExpYear(2022);
		testCard.setNumber("4069282136832346");
		
		testCard2 = new CreditCard();
		testCard2.setType("MasterCard");
		testCard2.setHolderFirstName("Bob");
		testCard2.setHolderLastName("Smith");
		testCard2.setExpMonth(2);
		testCard2.setExpYear(2023);
		testCard2.setNumber("5130631072050127");
		
		List<CreditCard> cardList = new LinkedList<>();
		cardList.add(testCard);
		cardList.add(testCard2);
		
		when(creditRepo.findByUserId(1)).thenReturn(cardList);
		
		List<CreditCard> resultList = creditServ.findByUserId(1);
		
		assertEquals(2, resultList.size());
		assertEquals("MasterCard", resultList.get(1).getType());
	}
	
	@Test
	void shouldReturnEmptyListIfUserHasNoCards() {
		List<CreditCard> cardList = Collections.emptyList();
		when(creditRepo.findByUserId(1)).thenReturn(cardList);
		List<CreditCard> resultList = creditServ.findByUserId(1);
		assertEquals(0, resultList.size());
	}
	
	@Test
	void shouldReturnEmptyListIfInvalidUserId() {
		List<CreditCard> resultList = creditServ.findByUserId(0);
		assertEquals(0, resultList.size());
	}
	
	@Test
	void shouldBeAbleToGetCreditCardById() {
		testCard = new CreditCard();
		testCard.setId(1);
		testCard.setType("Visa");
		testCard.setHolderFirstName("Bob");
		testCard.setHolderLastName("Smith");
		testCard.setExpMonth(9);
		testCard.setExpYear(2022);
		testCard.setNumber("4069282136832346");
		
		when(creditRepo.findById(1)).thenReturn(Optional.of(testCard));
		
		CreditCard result = creditServ.findById(testCard.getId());
		assertEquals(testCard, result);		
	}
	
	@Test
	void shouldNotGetCreditCardForInvalidId() {
		CreditCard u = creditServ.findById(0);
		assertNull(u);
	}
	
	@Test
	void shouldNotUpdateIfCreditCardNotFound() {
		testCard = new CreditCard();
		testCard.setId(1);
		when(creditRepo.findById(1)).thenReturn(Optional.empty());
		CreditCard card = creditServ.update(testCard);
		assertNull(card);
	}
	
	@Test
	void shouldUpdateCreditCard() {
		testCard = new CreditCard();
		testCard.setType("Visa");
		testCard.setHolderFirstName("Bob");
		testCard.setHolderLastName("Smith");
		testCard.setExpMonth(9);
		testCard.setExpYear(2022);
		testCard.setNumber("4069282136832346");
		
		testCard2 = new CreditCard();
		testCard2.setType("MasterCard");
		testCard2.setHolderFirstName("Mike");
		testCard2.setHolderLastName("Devlin");
		testCard2.setExpMonth(2);
		testCard2.setExpYear(2023);
		testCard2.setNumber("5130631072050127");
		testCard2.setId(1);
		
		when(creditRepo.findById(1)).thenReturn(Optional.of(testCard));
		creditServ.update(testCard2);
		
		assertEquals("5130631072050127", testCard.getNumber());
		
	}
	
	@Test
	void shouldNotUpdateCreditCardIfNoNewData() {
		testCard = new CreditCard();
		testCard.setType("Visa");
		testCard.setHolderFirstName("Bob");
		testCard.setHolderLastName("Smith");
		testCard.setExpMonth(9);
		testCard.setExpYear(2022);
		testCard.setNumber("4069282136832346");
		
		testCard2 = new CreditCard();
		testCard2.setId(1);
		
		when(creditRepo.findById(1)).thenReturn(Optional.of(testCard));
		creditServ.update(testCard2);
		
		assertEquals("4069282136832346", testCard.getNumber());
		
	}
}
