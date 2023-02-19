package com.avega.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.avega.task.dto.ApiStatus;
import com.avega.task.dto.ProductListResponse;
import com.avega.task.dto.ProductResponse;
import com.avega.task.models.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class TProductControllerTest extends AbstractTest {

	@Override
	@Before	
	public void setUp() {
		super.setUp();
	}

	@Test
	public void aCreateProduct() throws Exception {
		
		String url = "/v1/endpoint/product/add-product";
		Product product = new Product();
		
		product.productName = "Ginger";
		product.productUnit = "Kg";
		product.mrp = 80.00;
		product.price = 70.00;
		product.tax = 0.00;

		String addProductRequest = objectMapper.writeValueAsString(product);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(addProductRequest)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(201, status);

		String responseStr = mvcResult.getResponse().getContentAsString();

		ProductResponse response = objectMapper.readValue(responseStr, ProductResponse.class);
		assertEquals(response.message, "Product created succesfully");
		assertNotNull(response.product);
		assertNotEquals(response.product.id, 0);
		GlobalDataStore.pId = response.product.id;
	}

	@Test	
	public void bGetProductList() throws Exception {
		System.out.println("on get all product - " + GlobalDataStore.pId);
		String url = "/v1/endpoint/product/get-products";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String responseStr = mvcResult.getResponse().getContentAsString();

		ProductListResponse productResponse = objectMapper.readValue(responseStr, ProductListResponse.class);
		assertEquals(productResponse.status, 200);
		assertEquals(productResponse.message, "Product List fetched successfully");
		assertTrue(productResponse.productList.size() > 0);
	}

	@Test
	public void cGetProductById() throws Exception {			
		String url = "/v1/endpoint/product/get-product?productId="+String.valueOf(GlobalDataStore.pId);		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String responseStr = mvcResult.getResponse().getContentAsString();

		ProductResponse productResponse = objectMapper.readValue(responseStr, ProductResponse.class);
		assertNotNull(productResponse);
		assertEquals(200, productResponse.status);
		assertEquals(productResponse.message, "Product fetched successfully");
		assertNotNull(productResponse.product);
		assertEquals(GlobalDataStore.pId, productResponse.product.id);
	}
	
	@Test
	public void dUpdateProduct() throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		String url = "/v1/endpoint/product/update-product/"+String.valueOf(GlobalDataStore.pId);
		Product product = new Product();
		
		product.productName = "Ginger";
		product.productUnit = "Kg";
		product.mrp = 80.00;
		product.price = 20.00;
		product.tax = 0.00;

		String updateProductRequest = objectMapper.writeValueAsString(product);

		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(updateProductRequest)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String responseStr = mvcResult.getResponse().getContentAsString();

		ProductResponse response = objectMapper.readValue(responseStr, ProductResponse.class);
		assertEquals(response.message, "Product updated successfully!");
		assertNotNull(response.product);
		assertNotEquals(response.product.id, 0);
		assertNotEquals(response.product.price,70); // price updated
	}
	
	@Test
	public void eDeleteProduct() throws Exception{
		String url = "/v1/endpoint/product/delete-product?productId="+String.valueOf(GlobalDataStore.pId);
		
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);

		String responseStr = mvcResult.getResponse().getContentAsString();

		ApiStatus productDeleteResponse = objectMapper.readValue(responseStr, ApiStatus.class);
		assertNotNull(productDeleteResponse);
		assertEquals(200, productDeleteResponse.status);
		assertEquals(productDeleteResponse.message, "Product deleted successfully!");		
	}

}
