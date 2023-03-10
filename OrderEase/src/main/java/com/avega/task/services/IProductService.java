package com.avega.task.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.avega.task.dto.ApiStatus;
import com.avega.task.dto.ProductResponse;
import com.avega.task.models.Product;

@Service
public interface IProductService {

	ProductResponse addProduct(Product product);
	
	List<Product> getProducts();

	ApiStatus deleteById(int productId);

	Product getProductById(int productId);

	Product updateProduct(long productId, Product product);
}
