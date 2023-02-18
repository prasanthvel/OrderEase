package com.avega.task.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avega.task.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	Optional<List<Order>> findByCustomerId (int customerId);
}