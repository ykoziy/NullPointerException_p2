package com.modfathers.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.modfathers.enums.OrderStatus;
import com.modfathers.exception.DataNotFoundException;
import com.modfathers.model.CreditCard;
import com.modfathers.model.Order;
import com.modfathers.model.Payment;
import com.modfathers.model.Product;
import com.modfathers.model.User;
import com.modfathers.repository.CreditCardRepository;
import com.modfathers.repository.OrderRepository;
import com.modfathers.repository.ProductRepository;
import com.modfathers.repository.UserRepository;

@Service
public class OrderService {

	private final UserRepository userRepo;
	private final OrderRepository orderRepo;
	private final CreditCardRepository cardRepo;
	private final ProductRepository prodRepo;
	private final PaymentService payServ;

	@Autowired
	public OrderService(UserRepository userRepo, OrderRepository orderRepo, CreditCardRepository cardRepo,
			ProductRepository prodRepo, PaymentService payServ) {
		this.userRepo = userRepo;
		this.orderRepo = orderRepo;
		this.cardRepo = cardRepo;
		this.prodRepo = prodRepo;
		this.payServ = payServ;
	}

	public List<Order> findByUserId(int user_id) {
		if (user_id <= 0) {
			return Collections.emptyList();
		}	
		return orderRepo.findByUserId(user_id);
	}
	
	@Transactional
	public Order add(int user_id, int card_id, Order order) {
		User u = userRepo.findById(user_id).orElse(null);
		if (u != null) {
			CreditCard card = cardRepo.findById(card_id).orElse(null);
			if (card != null) {
				LocalDateTime dt = LocalDateTime.now();
				order.setStatus(OrderStatus.OPEN);
				order.setUpdateDate(dt);
				order.setOrderDate(dt);
				order.setUser(u);
				for (Product prod: order.getProducts()) {
					Product storedProd = prodRepo.findById(prod.getId()).orElse(null);
					if (storedProd != null) {
						int quantity = storedProd.getInventory();
						if (quantity == 0) {
							throw new DataNotFoundException("Product outs of stock, id: " + prod.getId());
						} else {
							storedProd.setInventory(--quantity);
						}
					} else {
						throw new DataNotFoundException("Did not find product with id: " + prod.getId());
					}
				}
				Payment payment = new Payment(card, order.getAmount(), "pending", dt);
				order.setPayment(payment);
				Order newOrder =  orderRepo.save(order);
				payServ.add(card_id, newOrder.getId(), payment);
				return newOrder;
			} else {
				throw new DataNotFoundException("Did not find credit card with id: " + card_id);
			}
		} else {
			throw new DataNotFoundException("Did not find user with id: " + user_id);
		}
	}
}
