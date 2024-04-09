package com.kh.test.user.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.test.user.model.dto.User;
import com.kh.test.user.model.mapper.UserMapper;



@Service
public class UserServiceImpl implements UserService{
	
	@Autowired // DI
	private UserMapper mapper;
	
	@Override
	public User search(String userId) {
		
		
		return mapper.search(userId);
	}
}
