package com.kh.test.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.test.user.model.dto.User;
import com.kh.test.user.model.service.UserService;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;


@Controller
public class UserController {

	
	@Autowired
	private UserService service;

	@RequestMapping("/")
	public String mainPage() {
		return "/index";
		
	}
	
	
	/**
	 * 아이디 검색
	 * @param userId
	 * @return
	 */
	@GetMapping("/search")
	@ResponseBody
	public String search(@RequestParam("userId") String userId, Model model) {
		
		
	
		User user = service.search(userId);
		
		if(user != null) {
			
			
		}
	
		
		return "redirect:/search";
		
	
		
		
	
		
	}
}
