package com.avega.task.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UIController {

	@GetMapping("/index")
	public ModelAndView indexPage() {
		ModelAndView mv = new ModelAndView("home");
		return mv;
	}

	@GetMapping("/load-customer-page")
	public ModelAndView customerPage() {
		ModelAndView mv = new ModelAndView("customer");
		return mv;
	}

	@GetMapping("/loadorderspage")
	public ModelAndView ordersDetailPage(@RequestParam("customerId") String customerId,
			@RequestParam("orderId") String orderId) {
		ModelAndView mv = new ModelAndView("order-details");
		mv.addObject("customerId", customerId);
		mv.addObject("orderId", orderId);
		return mv;
	}

	@GetMapping("/loadorderlist")
	public ModelAndView ordersListPage(@RequestParam("customerId") String customerId) {
		ModelAndView mv = new ModelAndView("order-list");
		mv.addObject("customerId", customerId);
		return mv;
	}

	@GetMapping("/customerslist")
	public ModelAndView getCustomersList() {
		ModelAndView mv = new ModelAndView("customers-list");
		return mv;
	}
	
	@GetMapping("/products")
	public ModelAndView getProductPage() {
		ModelAndView mv = new ModelAndView("products");
		return mv;
	}
}
