package com.avega.task.dto;

import com.avega.task.models.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CustomerResponse extends ApiStatus{
	public Customer customer;
}
