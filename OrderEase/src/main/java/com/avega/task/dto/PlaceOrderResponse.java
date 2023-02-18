package com.avega.task.dto;

import com.avega.task.models.Order;

public class PlaceOrderResponse extends ApiDTO {

	public int customerId;
	public int orderId;
	public Order order; 
}
