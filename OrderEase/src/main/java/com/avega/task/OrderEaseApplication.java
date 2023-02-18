package com.avega.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Doc", version ="1.0",description="MicroServices"))
public class OrderEaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderEaseApplication.class, args);
	}

}
