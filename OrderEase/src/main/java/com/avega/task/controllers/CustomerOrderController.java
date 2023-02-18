package com.avega.task.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.avega.task.dto.ApiDTO;
import com.avega.task.dto.CustomerListResponse;
import com.avega.task.dto.CustomerResponse;
import com.avega.task.dto.OrderResponse;
import com.avega.task.dto.PlaceOrderResponse;
import com.avega.task.models.Customer;
import com.avega.task.services.ICustomerService;
import com.avega.task.services.IOrderService;

@Controller
@RequestMapping(path = "${v1API}/customer")
public class CustomerOrderController {

	@Autowired
	private ICustomerService customerService;

	@Autowired
	private IOrderService orderService;

	@PostMapping("/place-order")
	@ResponseBody
	public PlaceOrderResponse placeOrder(@RequestBody Customer customer){		
		
		PlaceOrderResponse response = new PlaceOrderResponse();
		
		try {
			response = customerService.placeOrder(customer);			
			if (0 != response.orderId) {				
				response.message = "Order placed successfully!";
				response.status = 201;
			} else {
				response.message = "Failed to place order. Try again!";
				response.status = 200;
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			response.message = "An error occurred while saving order details.";
			response.status = 500;
			return response;
		}
	}
	
//	public PlaceOrderResponse saveCustomerAndOrder(@RequestBody Customer customer) {
//		PlaceOrderResponse response = new PlaceOrderResponse();
//		// validate Request body or all data
//		try {
//			response = customerService.saveCustomerAndOrder(customer);
//			if (0 != response.orderId) {
//				response.message = "Order placed successfully!";
//				response.status = 201;
//			} else {
//				response.message = "Failed to place order. Try again!";
//				response.status = 200;
//			}
//			return response;
//		} catch (Exception e) {
//			response.message = "An error occurred while saving order details.";
//			response.status = 500;
//			return response;
//		}
//	}

	@PostMapping("/add-customer")
	@ResponseBody
	public ApiDTO addCustomer(@RequestBody Customer customer) {
		ApiDTO response = new ApiDTO();
		try {
			customerService.saveCustomer(customer);
			response.message = "Customer added successfully!";
			response.status = 200;
			//return response;
		} catch (Exception e) {
			response.message = "Something went wrong!";
			response.status = 500;
			//return response;
		}
		return response;
	}

	@GetMapping("get-customers")
	@ResponseBody
	public CustomerListResponse getAllCustomers() throws Exception {
		try {

			List<Customer> customers = customerService.getAllCustomers();
			CustomerListResponse response = new CustomerListResponse();
			response.customerList = customers;
			if (response.customerList.size() > 0) {
				response.status = 200;
				response.message = "Customer List fetched successfully";
			} else {
				response.status = 204;
				response.message = "No Customers found!";
			}
			return response;

		} catch (Exception e) {
			CustomerListResponse response = new CustomerListResponse();
			response.status = 500;
			response.message = "Failed to fetch products!";
			return response;
		}
	}

	@GetMapping("get-customer-detail")
	@ResponseBody
	public CustomerResponse getCustomerDetail(@RequestParam("customerId") int customerId) throws Exception {
		try {

			Optional<Customer> customer = customerService.getCustomerDetail(customerId);
			CustomerResponse response = new CustomerResponse();

			if (customer.isPresent()) {
				response.status = 200;
				response.message = "Customer List fetched successfully";
				response.customer = customer.get();

			} else {
				response.status = 204;
				response.message = "No Customer found for the given input";
			}
			return response;

		} catch (Exception e) {
			CustomerResponse response = new CustomerResponse();
			response.status = 500;
			response.message = "Something went wrong while fetching customer detail!";
			return response;
		}
	}

	@GetMapping("get-order-detail")
	@ResponseBody
	public OrderResponse getOrderDetails(@RequestParam("orderId") int orderId) {

		OrderResponse response = new OrderResponse();

		try {
			response.order = orderService.getOrderDetail(orderId);
			response.status = 200;
			response.message = "Order details fetched successfully!";
			if (response.order.id == 0) {
				response.status = 204;
				response.message = "No data found for the given order id!";
				response.order = null;
			}
			return response;
		} catch (Exception e) {
			response.status = 500;
			response.message = "Something went wrong!";
			return response;
		}
	}

	@GetMapping("get-customer-orders")
	@ResponseBody
	public ResponseEntity<CustomerResponse> /* CustomerResponse */ getCustomerOrders(
			@RequestParam("customerId") int customerId) throws Exception {
		try {
			Optional<Customer> optionalCustomer = customerService.getCustomerDetail(customerId);

			CustomerResponse response = new CustomerResponse();

			if (optionalCustomer.isPresent()) {
				Customer customer = customerService.getCustomerOrders(customerId);
				response.status = 200;
				response.message = "Customer List fetched successfully";
				response.customer = customer;
				return ResponseEntity.status(HttpStatus.OK).body(response);

			} else {
				response.status = 204;
				response.message = "No Customer found for the given input";
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}

		} catch (Exception e) {
			CustomerResponse response = new CustomerResponse();
			response.status = 500;
			response.message = "Something went wrong while fetching customer detail!";
			// return response;
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@DeleteMapping("delete-order")
	@ResponseBody
	public ResponseEntity<ApiDTO> deleteAnOrder(@RequestParam("orderId") int orderId) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(orderService.deleteOrder(orderId));
		} catch (Exception e) {
			ApiDTO response = new ApiDTO();
			response.status = 500;
			response.message = "Something went wrong while deleting order!";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}
