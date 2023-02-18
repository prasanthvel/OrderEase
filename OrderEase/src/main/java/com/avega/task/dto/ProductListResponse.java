package com.avega.task.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import com.avega.task.models.Product;

@Component
public class ProductListResponse extends ApiDTO{

	public List<Product> productList;
}
