package com.avega.task.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avega.task.models.OrderDetail;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Long> {

	Optional<List<OrderDetail>> findByOrderId (int orderId);
	
	void deleteByOrderId(int orderId);
}
