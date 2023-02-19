package com.avega.task.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.avega.task.dto.ApiStatus;
import com.avega.task.dto.ProductListResponse;
import com.avega.task.dto.ProductResponse;
import com.avega.task.models.Product;
import com.avega.task.services.IProductService;

@Controller
@RequestMapping(path = "${v1API}/product")
public class ProductController {

	@Autowired
	private IProductService productService;

	@PostMapping("/add-product")
	@ResponseBody
	public ResponseEntity<ProductResponse> addProduct(@RequestBody Product product) {
		ProductResponse response = new ProductResponse();
		try {
			response = productService.addProduct(product);
			if (response.status == 201)
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			else
				return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (DuplicateKeyException e) {
			response.status = 200;
			response.message = "Product already exists";
			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (Exception e) {
			response.status = 500;
			response.message = "Something went wrong!";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/get-products")
	@ResponseBody
	public ResponseEntity<ProductListResponse> getAllProducts() throws Exception {

		ProductListResponse response = new ProductListResponse();

		try {

			List<Product> products = productService.getProducts();

			response.productList = products;
			if (response.productList.size() > 0) {
				response.status = 200;
				response.message = "Product List fetched successfully";
			} else {
				response.status = 204;
				response.message = "No Products found!";
			}

			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (Exception e) {

			response.status = 500;
			response.message = "Failed to fetch products!";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

		}
	}

	@GetMapping("/get-product")
	@ResponseBody
	public ResponseEntity<ProductResponse> getProduct(@RequestParam(name="productId",required=true) int productId) throws Exception {
		try {
			System.out.println("inside get product - " + productId);
			ProductResponse response = new ProductResponse();

			Product product = productService.getProductById(productId);

			if (0 != product.id) {
				response.status = 200;
				response.message = "Product fetched successfully";
				response.product = product;
			} else {
				response.status = 204;
				response.message = "No product found for given id!";
			}
			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (Exception e) {
			ProductResponse response = new ProductResponse();

			response.status = 500;
			response.message = "Failed to fetch products!";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/update-product/{productId}")
	@ResponseBody
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable long productId, @RequestBody Product product) {
		ProductResponse response = new ProductResponse();
		try {
			Product updatedProduct = productService.updateProduct(productId, product);
			if (updatedProduct.id != 0) {
				response.status = 200;
				response.message = "Product updated successfully!";
				response.product = updatedProduct;
			} else {
				response.status = 204;
				response.message = "No product found for given id!";
			}
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response.status = 500;
			response.message = "Something went wrong!";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/delete-product")
	@ResponseBody
	public ResponseEntity<ApiStatus> deleteProduct(@RequestParam("productId") int productId) throws Exception {
		ApiStatus response = new ApiStatus();
		try {
			response = productService.deleteById(productId);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response.status = 500;
			response.message = "Something went wrong!";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
