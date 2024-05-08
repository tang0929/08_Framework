package com.kh.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.test.model.dto.Student;
import com.kh.test.model.service.StudentService;

@Controller
@RequestMapping("student")
public class StudentController {
	
	@Autowired
	private StudentService service;
	
	
	@ResponseBody
	@GetMapping("selectStudentList")
	public List<Student> selectStudentList(){
		
		return service.selectStudentList();
	}

	
	
}
