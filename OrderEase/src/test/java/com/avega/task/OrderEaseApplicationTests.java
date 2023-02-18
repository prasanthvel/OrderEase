package com.avega.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avega.task.models.Product;
import com.avega.task.repositories.ProductRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@TestMethodOrder(OrderAnnotation.class)
class OrderEaseApplicationTests {

	@Autowired
	ProductRepository productRepo;

	private int pId;

	@Test
	@Order(1)
	public void testCreateProduct() {
		Product product = new Product();
		product.id = 1;
		product.productName = "Carlsberg";
		product.productUnit = "ltr";
		product.mrp = 200.00;
		product.price = 180.00;
		product.tax = 18.00;
		pId = productRepo.saveAndFlush(product).id;
		System.out.println(pId);
		assertNotNull(productRepo.findById(pId));
	}
	
	@Test
	@Order(2)
	public void testGetAllProducts() {		
		List<Product> productList = productRepo.findAll();
		assertThat(productList).size().isGreaterThan(0);
	}

	@Test
	@Order(3)
	public void testGetProduct() {
		System.out.println(pId);
		// set product id to get test success
		Product product = productRepo.findById(pId).get();
		assertEquals(180, product.price);
	}

	@Test
	@Order(4)
	public void testProductUpdate() {
		System.out.println(pId);
		Product product = productRepo.findById(pId).get();
		product.price = 100.00;
		productRepo.save(product);
		assertNotEquals(180, productRepo.findById(pId).get().price);
	}

	@Test
	@Order(5)
	public void testProductDelete() {
		System.out.println(pId);
		productRepo.deleteById((long) pId);
		assertThat(productRepo.existsById((long) pId)).isFalse();
	}
}
