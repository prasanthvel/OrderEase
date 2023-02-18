package com.avega.task.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avega.task.dto.ApiDTO;
import com.avega.task.dto.ProductResponse;
import com.avega.task.models.Product;
import com.avega.task.repositories.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements IProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public ProductResponse addProduct(Product product) {
		Optional<Product> existingProduct = productRepository.findById(product.getId());
		ProductResponse response = new ProductResponse();

		if (existingProduct.isPresent()) {
			product.id = existingProduct.get().id;
			response.status = 200;
			response.message = "Product updated succesfully";
		} else {
			response.status = 201;
			response.message = "Product created succesfully";
		}

		response.product = productRepository.saveAndFlush(product);
		return response;
	}

	@Override
	public List<Product> getProducts() {
		List<Product> productList = productRepository.findAll();
		return productList;
	}

	@Override
	@Transactional
	public ApiDTO deleteById(int productId) {
		ApiDTO response = new ApiDTO();
		if (productRepository.existsById((long) productId)) {
			productRepository.deleteById((long) productId);
			response.status = 200;
			response.message = "Product deleted successfully!";
		} else {
			response.status = 204;
			response.message = "Product not found for given id!";
		}
		return response;
	}

	@Override
	public Product getProductById(int productId) {
		Optional<Product> tempProduct = productRepository.findById((long) productId);
		Product product = new Product();
		if (tempProduct.isPresent()) {
			return tempProduct.get();
		} else {
			return product;
		}
	}

	@Override
	public Product updateProduct(long productId, Product product) {
		Product updatedProduct = new Product();
		if (productRepository.findById(productId).isPresent()) {
			updatedProduct = productRepository.saveAndFlush(product);
		}
		return updatedProduct;
	}
}
