package com.avega.task.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avega.task.dto.ApiDTO;
import com.avega.task.models.Order;
import com.avega.task.models.OrderDetail;
import com.avega.task.repositories.OrderDetailsRepository;
import com.avega.task.repositories.OrderRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDetailsRepository orderDetailRepository;

	@Override
	public Order getOrderDetail(int orderId) {

		Optional<Order> tempOrder = orderRepository.findById((long) orderId);
		Order order = new Order();
		if (tempOrder.isPresent()) {
			order = tempOrder.get();
			Optional<List<OrderDetail>> orderDetails = orderDetailRepository.findByOrderId(order.getId());

			if (orderDetails.isPresent()) {
				order.orderDetails = orderDetails.get();
			}
		}
		return order;
	}

	@Override
	public ApiDTO deleteOrder(int orderId) {
		ApiDTO response = new ApiDTO();
		try {
			orderDetailRepository.deleteByOrderId(orderId);
			orderRepository.deleteById((long) orderId);
			response.status = 200;
			response.message = "Order Deleted successfully!";
		}catch(Exception e) {
			response.status = 500;
			response.message = "Something went wrong!";
		}
		return response;
	}

}
