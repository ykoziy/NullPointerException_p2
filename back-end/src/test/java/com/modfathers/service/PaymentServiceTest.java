package com.modfathers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.modfathers.model.CreditCard;
import com.modfathers.model.Payment;
import com.modfathers.repository.CreditCardRepository;
import com.modfathers.repository.PaymentRepository;

// loads junit extension
@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
	
	//mock is created for this variable
	@Mock
	private PaymentRepository payRepo;
	
	@Mock
	private CreditCardRepository cardRepo;
	
	private PaymentService payServ;
	private Payment testPayment;
	private Payment testPayment2;
	private CreditCard testCard;
	
	
	@BeforeEach
	void beforeEach() {
		payServ = new PaymentService(payRepo, cardRepo);
	}
	
	@AfterEach
	void cleanUp() {
		testPayment = null;
		testCard = null;
		testPayment2 = null;
	}
	
	
	@Test
	void shouldBeAbleToAddPaymentForCreditCard() {
		testCard = new CreditCard();
		testCard.setType("Visa");
		testCard.setHolderFirstName("Bob");
		testCard.setHolderLastName("Smith");
		testCard.setExpMonth(9);
		testCard.setExpYear(2022);
		testCard.setNumber("4069282136832346");
		testCard.setId(1);
		
		testPayment = new Payment(testCard, 1269.99, "pending", null);
		
		when(cardRepo.findById(1)).thenReturn(Optional.of(testCard));
		when(payRepo.save(testPayment)).thenReturn(testPayment);
		
		Payment pay = payServ.add(1, testPayment);
		
		assertEquals(testCard, pay.getCard());
		assertEquals(testCard.getNumber(), pay.getCard().getNumber());
		assertEquals("pending", pay.getState());
	}
	
	@Test
	void shouldGetPaymentById() {
		testCard = new CreditCard();
		testCard.setType("Visa");
		testCard.setHolderFirstName("Bob");
		testCard.setHolderLastName("Smith");
		testCard.setExpMonth(9);
		testCard.setExpYear(2022);
		testCard.setNumber("4069282136832346");
		testCard.setId(2);
		
		testPayment = new Payment(1, testCard, 1269.99, "pending", LocalDateTime.now());
		when(payRepo.findById(1)).thenReturn(Optional.of(testPayment));
		Payment newPayment = payServ.findById(1);
		assertEquals(testCard, newPayment.getCard());
	}
	
	@Test
	void shouldUpdatePayment() {
		testCard = new CreditCard();
		testCard.setType("Visa");
		testCard.setHolderFirstName("Bob");
		testCard.setHolderLastName("Smith");
		testCard.setExpMonth(9);
		testCard.setExpYear(2022);
		testCard.setNumber("4069282136832346");
		testCard.setId(2);
		
		LocalDateTime current = LocalDateTime.now();
		testPayment = new Payment(1, testCard, 1269.99, "pending", current);
		testPayment2 = new Payment(1, testCard, 1000.99, "cleared", current);
		
		when(payRepo.findById(1)).thenReturn(Optional.of(testPayment));
		payServ.update(testPayment2);
		assertEquals(1000.99, testPayment.getAmount());
		assertEquals("cleared", testPayment.getState());
		assertEquals(testPayment.getId(), testPayment2.getId());
		assertEquals(testPayment.getTimestamp(), testPayment2.getTimestamp());
	}
	
}