package com.kh.test.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.test.model.dto.Student;
import com.kh.test.model.mapper.StudentMapper;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentMapper mapper;
	
	@Override
	public List<Student> selectStudentList() {
		
		return mapper.selectStudentList();
	}

}
