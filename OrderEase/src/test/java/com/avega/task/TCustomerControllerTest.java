package com.avega.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.avega.task.dto.ApiStatus;
import com.avega.task.dto.CustomerListResponse;
import com.avega.task.dto.CustomerResponse;
import com.avega.task.dto.OrderResponse;
import com.avega.task.dto.PlaceOrderResponse;
import com.avega.task.models.Customer;
import com.avega.task.models.Order;
import com.avega.task.models.OrderDetail;

@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TCustomerControllerTest extends AbstractTest {

	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void aAddCustomer() throws Exception {

		String url = "/v1/endpoint/customer/add-customer";

		Customer customer = new Customer();

		customer.name = "James";
		customer.mobileNumber = "9988776655";
		customer.emailId = "james@gmail.com";
		customer.address = "1st Street, City";

		String addCustomerRequest = objectMapper.writeValueAsString(customer);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(addCustomerRequest)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String responseStr = mvcResult.getResponse().getContentAsString();

		CustomerResponse response = objectMapper.readValue(responseStr, CustomerResponse.class);
		assertEquals(response.status, 200);
		assertEquals(response.message, "Customer added successfully!");

		GlobalDataStore.cId = response.customer.id;
	}

	@Test
	public void bGetCustomers() throws Exception {

		String url = "/v1/endpoint/customer/get-customers";

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String responseStr = mvcResult.getResponse().getContentAsString();

		CustomerListResponse customerListResponse = objectMapper.readValue(responseStr, CustomerListResponse.class);
		assertEquals(customerListResponse.status, 200);
		assertEquals(customerListResponse.message, "Customer List fetched successfully");
		assertNotNull(customerListResponse.customerList);
		assertTrue(customerListResponse.customerList.size() > 0);
	}

	@Test
	public void cGetCustomerDetailById() throws Exception {
		String url = "/v1/endpoint/customer/get-customer-detail?customerId=" + String.valueOf(GlobalDataStore.cId);
		;

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String responseStr = mvcResult.getResponse().getContentAsString();

		CustomerResponse customerResponse = objectMapper.readValue(responseStr, CustomerResponse.class);
		assertNotNull(customerResponse);
		assertEquals(200, customerResponse.status);
		assertEquals("Customer List fetched successfully!", customerResponse.message);
		assertNotNull(customerResponse.customer);
		assertEquals(GlobalDataStore.cId, customerResponse.customer.id);

	}

	@Test
	public void dPlaceOrder() throws Exception {
		String url = "/v1/endpoint/customer/place-order";

		Customer customer = new Customer();

		customer.name = "James";
		customer.mobileNumber = "9988776655";
		customer.emailId = "james@gmail.com";
		customer.address = "1st Street, City";

		customer.orders = new ArrayList<Order>();

		Order orderItem = new Order();
		customer.orders.add(orderItem);

		customer.orders.get(0).orderDate = "19-02-2023:02:08 AM";
		customer.orders.get(0).orderStatus = "Placed";

		List<OrderDetail> orderList = new ArrayList<OrderDetail>();

		OrderDetail orderDetailItem = new OrderDetail();

		orderDetailItem.productName = "Ginger";
		orderDetailItem.productUnit = "Kg";
		orderDetailItem.mrp = 80.00;
		orderDetailItem.price = 70.00;
		orderDetailItem.quantity = 2.00;
		orderDetailItem.subTotal = orderDetailItem.price * orderDetailItem.quantity;

		orderList.add(orderDetailItem);

		orderDetailItem = new OrderDetail();
		orderDetailItem.productName = "Apple";
		orderDetailItem.productUnit = "Kg";
		orderDetailItem.mrp = 180.00;
		orderDetailItem.price = 150.00;
		orderDetailItem.quantity = 2.00;
		orderDetailItem.subTotal = orderDetailItem.price * orderDetailItem.quantity;

		orderList.add(orderDetailItem);

		customer.orders.get(0).orderDetails = orderList;

		int _totalAmount = 0;

		for (OrderDetail p : customer.orders.get(0).orderDetails) {
			_totalAmount += p.subTotal;
		}

		customer.orders.get(0).totalAmount = _totalAmount;

		String placeOrderRequest = objectMapper.writeValueAsString(customer);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(placeOrderRequest)).andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertTrue(status == 201 || status == 200);

		String responseStr = mvcResult.getResponse().getContentAsString();

		PlaceOrderResponse response = objectMapper.readValue(responseStr, PlaceOrderResponse.class);

		assertTrue(response.status == 201 || response.status == 200);
		assertEquals("Order placed successfully!", response.message);
		assertNotEquals(0, response.customerId);
		assertNotEquals(0, response.orderId);
		assertNotNull(response.order);
		assertNotNull(response.order.orderDetails.size() > 0);

		GlobalDataStore.oId = response.orderId;
	}

	@Test
	public void eGetOrderDetailsById() throws Exception {

		String url = "/v1/endpoint/customer/get-order-detail?orderId=" + String.valueOf(GlobalDataStore.oId);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String responseStr = mvcResult.getResponse().getContentAsString();

		OrderResponse ordersResponse = objectMapper.readValue(responseStr, OrderResponse.class);
		assertEquals(200, ordersResponse.status);
		assertEquals("Order details fetched successfully!", ordersResponse.message);
		assertNotNull(ordersResponse.order);
		assertEquals(GlobalDataStore.oId, ordersResponse.order.id);
		assertTrue(ordersResponse.order.orderDetails.size() > 0);

	}

	@Test
	public void fGetCustomerOrders() throws Exception {
		String url = "/v1/endpoint/customer/get-customer-orders?customerId=" + String.valueOf(GlobalDataStore.cId);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String responseStr = mvcResult.getResponse().getContentAsString();

		CustomerResponse customerOrdersResponse = objectMapper.readValue(responseStr, CustomerResponse.class);
		assertNotNull(customerOrdersResponse);
		assertEquals(200, customerOrdersResponse.status);
		assertEquals("Customer order list fetched successfully!", customerOrdersResponse.message);
		assertNotNull(customerOrdersResponse.customer);
		assertEquals(GlobalDataStore.cId, customerOrdersResponse.customer.id);
		assertNotNull(customerOrdersResponse.customer.orders);
		assertTrue(customerOrdersResponse.customer.orders.size() > 0); // ...Satisfied only if orders available for the
																		// customer
	}

	@Test
	public void gUpdateOrderStatus() throws Exception {
		String url = "/v1/endpoint/customer/update-order-status/" + String.valueOf(GlobalDataStore.oId);

		Order updateOrder = new Order();
		updateOrder.orderStatus = "Shipped";

		String requestStr = objectMapper.writeValueAsString(updateOrder);

		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestStr))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String responseStr = mvcResult.getResponse().getContentAsString();

		OrderResponse updateResponse = objectMapper.readValue(responseStr, OrderResponse.class);
		assertNotNull(updateResponse);
		assertEquals(200, updateResponse.status);
		assertEquals("Order status updated successfully!", updateResponse.message);
		assertEquals("Shipped", updateResponse.order.orderStatus);
		assertTrue(updateResponse.order.orderDetails.size() > 0);
	}

	@Test
	public void hDeleteOrderById() throws Exception {
		String url = "/v1/endpoint/customer/delete-order?orderId=" + String.valueOf(GlobalDataStore.oId);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(url).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String responseStr = mvcResult.getResponse().getContentAsString();

		ApiStatus deleteResponse = objectMapper.readValue(responseStr, ApiStatus.class);
		assertNotNull(deleteResponse);
		assertEquals(200, deleteResponse.status);
		assertEquals("Order Deleted successfully!", deleteResponse.message);
	}
}
