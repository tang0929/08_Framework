package com.kh.test.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.test.customer.model.service.CustomerService;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("add")
public class CustomerController {

	@Autowired
	private CustomerService service;

	
	@RequestMapping("/")
	public String mainPage() {
		return "/index";
	}
	
	
	
	
	@PostMapping("result")
	public String addCustomer(@RequestParam("customerName") String customerName,
			@RequestParam("customerTel") String customerTel, 
			@RequestParam("customerAddress") String customerAddress) {
		
		int result = service.addCustomer(customerName, customerTel, customerAddress);
		
		
		
		
		String path = null;
		String message = null;
		
		if(result > 0) {
			
			path ="add/result";
			
	
		} else {
			
		
			return "redirect:/";
		}
		return path;
		
		
		
		
		
		
		
	}
	
	
	
}
