package com.nh.book.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nh.book.main.service.MainService;


@Controller

public class MainController {
	
	

	
	@RequestMapping("/")
	public String mainPage() {
		return "common/main";
	}
	

	
	
}
