package com.avega.task.services;

import org.springframework.stereotype.Service;

import com.avega.task.dto.ApiStatus;
import com.avega.task.dto.OrderResponse;
import com.avega.task.models.Order;

@Service
public interface IOrderService {

	Order getOrderDetail(int orderId);
	
	ApiStatus deleteOrder(int orderId);

	OrderResponse updateOrderStatus(int orderId, Order order);
}
