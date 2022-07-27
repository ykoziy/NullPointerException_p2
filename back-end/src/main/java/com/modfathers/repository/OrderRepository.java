package com.modfathers.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.modfathers.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	List<Order> findByUserId(int user_id);
}