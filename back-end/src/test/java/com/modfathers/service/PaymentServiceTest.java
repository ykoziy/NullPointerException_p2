package com.modfathers.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.modfathers.exception.DataNotFoundException;
import com.modfathers.model.Address;
import com.modfathers.model.CreditCard;
import com.modfathers.model.Order;
import com.modfathers.model.Payment;
import com.modfathers.repository.CreditCardRepository;
import com.modfathers.repository.OrderRepository;
import com.modfathers.repository.PaymentRepository;

// loads junit extension
@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
	
	//mock is created for this variable
	@Mock
	private PaymentRepository payRepo;
	
	@Mock
	private CreditCardRepository cardRepo;
	
	@Mock
	private OrderRepository orderRepo;
	
	private PaymentService payServ;
	private Payment testPayment;
	private Payment testPayment2;
	private CreditCard testCard;
	private Order testOrder;
	
	
	@BeforeEach
	void beforeEach() {
		payServ = new PaymentService(payRepo, cardRepo, orderRepo);
	}
	
	@AfterEach
	void cleanUp() {
		testPayment = null;
		testCard = null;
		testPayment2 = null;
		testOrder = null;
	}
	
	@Test
	void shouldNotAddPaymentIfNoCreditCardFound() {
		testPayment = new Payment();
		assertThrows(DataNotFoundException.class, () -> {
			payServ.add(12, testPayment);
		});
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
		
		testPayment = new Payment(testCard, 1269.99, "pending", testOrder);
		
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
		
		testPayment = new Payment(1, testCard, testOrder, 1269.99, "pendig", LocalDateTime.now());
		when(payRepo.findById(1)).thenReturn(Optional.of(testPayment));
		Payment newPayment = payServ.findById(1);
		assertEquals(testCard, newPayment.getCard());
	}
	
	@Test
	void shouldNotGetPaymentByInvalidId() {
		assertNull(payServ.findById(0));
	}
	
	@Test
	void shouldNotDeleteIfPaymentNotFound() {
		assertFalse(payServ.delete(100));
	}
	
	@Test
	void shouldDeleteIfPaymentFound() {
		testPayment = new Payment();
		testPayment.setId(100);
		when(payRepo.existsById(100)).thenReturn(true);
		assertTrue(payServ.delete(100));
	}
	
	@Test
	void shouldNotUpdateIfPaymentNotFound() {
		testPayment = new Payment();
		testPayment.setId(1);
		when(payRepo.findById(1)).thenReturn(Optional.empty());
		Payment p = payServ.update(testPayment);
		assertNull(p);
	}
	
	@Test
	void shouldNotUpdatePaymentIfNewDataEmpty() {
		testCard = new CreditCard();
		testCard.setType("Visa");
		testCard.setHolderFirstName("Bob");
		testCard.setHolderLastName("Smith");
		testCard.setExpMonth(9);
		testCard.setExpYear(2022);
		testCard.setNumber("4069282136832346");
		testCard.setId(2);
		
		LocalDateTime current = LocalDateTime.now();
		testPayment = new Payment(1, testCard, testOrder, 1269.99, "pendig", current);
		testPayment2 = new Payment();
		testPayment2.setId(1);
		
		when(payRepo.findById(1)).thenReturn(Optional.of(testPayment));
		payServ.update(testPayment2);
		assertEquals(testCard, testPayment.getCard());
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
		testPayment = new Payment(1, testCard, testOrder, 1269.99, "pending", current);
		testPayment2 = new Payment(1, testCard, testOrder, 1000.99, "cleared", current);
		
		when(payRepo.findById(1)).thenReturn(Optional.of(testPayment));
		payServ.update(testPayment2);
		assertEquals(1000.99, testPayment.getAmount());
		assertEquals("cleared", testPayment.getState());
		assertEquals(testPayment.getId(), testPayment2.getId());
		assertEquals(testPayment.getTimestamp(), testPayment2.getTimestamp());
	}
	
}
