package com.kh.test.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.test.customer.model.dto.Customer;
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
	public String addCustomer(Customer customer, Model model) {
		
		int result = service.addCustomer(customer);
		
		
		
		
	
		
		if(result > 0) {
			
			model.addAttribute("message","추가 성공");
			
	
		} else {
			
			model.addAttribute("message", "추가 실패...");
			return "result";
		}
		
		
		
		
		
		
		
		
	}
	
	
	
}
