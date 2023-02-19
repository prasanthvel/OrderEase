package com.avega.task.dto;

import com.avega.task.models.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderResponse extends ApiStatus{

	public Order order;
}
