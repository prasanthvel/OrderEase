package com.avega.task.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avega.task.dto.PlaceOrderResponse;
import com.avega.task.models.Customer;
import com.avega.task.models.Order;
import com.avega.task.models.OrderDetail;
import com.avega.task.repositories.CustomerRepository;
import com.avega.task.repositories.OrderDetailsRepository;
import com.avega.task.repositories.OrderRepository;

@Service
public class CustomerServiceImpl implements ICustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderDetailsRepository orderDetailsRepository;

	@Override
	public PlaceOrderResponse placeOrder(Customer customer) {		
		PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();

		Optional<Customer> existingCustomer = customerRepository.findByEmailIdOrMobileNumber(customer.getEmailId(),
				customer.getMobileNumber());

		// keep order details to avoid data loss
		List<Order> tempOrders = customer.orders;

		if (existingCustomer.isPresent()) {
			// existing customer - map order to existing customer
			customer.id = existingCustomer.get().id;
		} else {
			// create new user and proceed to place order
			customer = saveCustomer(customer);
			customer.orders = tempOrders;
		}

		Order order = createNewOrder(customer);

		System.out.println("order id in place order - " + order.id);
		placeOrderResponse.customerId = customer.id;

		if (0 != order.id) {

			placeOrderResponse.orderId = order.id;
			placeOrderResponse.order = order;

		}

		// if order not placed - return with customer id alone
		return placeOrderResponse;

	}
//	public PlaceOrderResponse saveCustomerAndOrder(Customer customer) {
//
//		Optional<Customer> existingCustomer = customerRepository.findByEmailIdOrMobileNumber(customer.getEmailId(),
//				customer.getMobileNumber());
//
//		PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();
//
//		if (existingCustomer.isPresent()) {
//			customer.id = existingCustomer.get().id;
//			// validate form data for existing user and update details?
//		} else {
//			Customer _cust = new Customer();
//			// to avoid exception while creating customer save customer details only, ignore
//			// order list
//			_cust.emailId = customer.emailId;
//			_cust.name = customer.name;
//			_cust.mobileNumber = customer.mobileNumber;
//			_cust.address = customer.address;
//
//			Customer temp = customerRepository.saveAndFlush(_cust);
//			customer.id = temp.id;
//		}
//
//		Order order = createNewOrder(customer);
//
//		placeOrderResponse.customerId = customer.id;
//
//		if (0 != order.id) {
//			placeOrderResponse.orderId = order.id;
//		}
//
//		// if order not placed - return with customer id alone
//		return placeOrderResponse;
//
//	}

	@Override
	public Customer saveCustomer(Customer customer) {
		Optional<Customer> existingCustomer = customerRepository.findByEmailIdOrMobileNumber(customer.getEmailId(),
				customer.getMobileNumber());

		Customer _cust = new Customer();
		// PlaceOrderResponse placeOrderResponse = new PlaceOrderResponse();

		if (existingCustomer.isPresent()) {
			// customer.id = existingCustomer.get().id;
			// validate form data for existing user and update details?
			_cust = existingCustomer.get();
		}

		_cust.emailId = customer.emailId;
		_cust.name = customer.name;
		_cust.mobileNumber = customer.mobileNumber;
		_cust.address = customer.address;

		customerRepository.saveAndFlush(_cust);

		return _cust;
	}

	public Order createNewOrder(Customer customer) {

		if (null != customer.orders && customer.orders.size() > 0) {

			customer.orders.get(0).customerId = customer.id;

			if (null != customer.orders.get(0).orderDetails && customer.orders.get(0).orderDetails.size() > 0) {
				// can place order
				Order tempOrder = new Order();

				tempOrder.customerId = customer.orders.get(0).customerId;
				tempOrder.orderDate = customer.orders.get(0).orderDate;
				tempOrder.orderStatus = customer.orders.get(0).orderStatus;
				tempOrder.totalAmount = customer.orders.get(0).totalAmount;

				Order temp = orderRepository.saveAndFlush(tempOrder);
				System.out.println("order placed - " + temp.id);
				createOrderDetails(customer.orders.get(0).orderDetails, temp);
				temp.orderDetails = customer.orders.get(0).orderDetails;
				return temp;
			} else {
				System.out.println("return empty order");
				return new Order();
			}
		} else {
			return new Order();
		}
	}

	public void createOrderDetails(List<OrderDetail> orderDetailList, Order order) {
		orderDetailList.forEach(p -> p.orderId = order.id);
		orderDetailsRepository.saveAll(orderDetailList);
	}

	@Override
	public List<Customer> getAllCustomers() {
		List<Customer> customerList = customerRepository.findAll();
		return customerList;

	}

	@Override
	public Optional<Customer> getCustomerDetail(int customerId) {
		Optional<Customer> customer = customerRepository.findById((long) customerId);
		return customer;
	}

	@Override
	public Customer getCustomerOrders(int customerId) {
		Optional<Customer> customer = customerRepository.findById((long) customerId);

		if (customer.isPresent()) {
			Optional<List<Order>> orderList = orderRepository.findByCustomerId(customer.get().getId());
			if (orderList.isPresent()) {

				orderList.get().forEach(o -> {
					o.setOrderDetails(getOrderDetails(o.getId()));
				});
				customer.get().setOrders(orderList.get());
				return customer.get();
			} else {
				return customer.get();
			}
		} else {
			return new Customer();
		}
	}

	private List<OrderDetail> getOrderDetails(int orderId) {
		Optional<List<OrderDetail>> optionalOrderDetails = orderDetailsRepository.findByOrderId(orderId);

		if (optionalOrderDetails.isPresent()) {
			return optionalOrderDetails.get();

		} else {
			return new ArrayList<OrderDetail>();
		}
	}

	@Override
	public void addCustomer(Customer customer) {
		Optional<Customer> existingCustomer = customerRepository.findByEmailIdOrMobileNumber(customer.getEmailId(),
				customer.getMobileNumber());
		boolean isValueChanged = false;
		if (existingCustomer.isPresent()) {
			// call customer update
			Customer temp = existingCustomer.get();
			if (null != customer.address && customer.address.isEmpty()) {
				if (!customer.address.toLowerCase().equals(temp.address.toLowerCase())) {
					temp.address = customer.address;
					isValueChanged = true;
				}
			}
			/*
			 * TODO validate user information before saving
			 */
			if (isValueChanged) {
				customerRepository.save(temp);
			}
		} else {
			customerRepository.save(customer);
		}

	}

}