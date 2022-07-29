package com.modfathers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.modfathers.model.Order;
import com.modfathers.service.OrderService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderServ;
	
	@GetMapping("/uid/{user_id}")
	public ResponseEntity<List<Order>> getByUserId(@PathVariable("user_id") int userId) {
		List<Order> orderList = orderServ.findByUserId(userId);
		if (orderList.isEmpty()) {
			return new ResponseEntity<List<Order>>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<List<Order>>(orderList, HttpStatus.OK);
		}
	}
	
	@PostMapping("/add/{user_id}/{card_id}")
	public Order addOrder(@PathVariable("user_id") int userId, @PathVariable("card_id") int cardId, @RequestBody Order order) {
		return orderServ.add(userId, cardId, order);
	}
}
