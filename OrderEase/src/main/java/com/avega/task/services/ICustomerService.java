package com.avega.task.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.avega.task.dto.PlaceOrderResponse;
import com.avega.task.models.Customer;

@Service
public interface ICustomerService {

	PlaceOrderResponse placeOrder(Customer customer);
	
	void addCustomer(Customer customer);
	
	Customer saveCustomer(Customer customer);
	
	List<Customer> getAllCustomers();
	
	Optional<Customer> getCustomerDetail(int customerId);
	
	Customer getCustomerOrders(int customerId);
	
}
