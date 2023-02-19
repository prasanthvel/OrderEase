package com.avega.task.dto;

import com.avega.task.models.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ProductResponse extends ApiStatus{
	public Product product; 
}
