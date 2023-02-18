package com.avega.task.services;

import org.springframework.stereotype.Service;

import com.avega.task.dto.ApiDTO;
import com.avega.task.models.Order;

@Service
public interface IOrderService {

	Order getOrderDetail(int orderId);
	
	ApiDTO deleteOrder(int orderId);
}
