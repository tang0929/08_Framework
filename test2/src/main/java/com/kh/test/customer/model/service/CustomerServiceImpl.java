package com.kh.test.customer.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.test.customer.model.dto.Customer;
import com.kh.test.customer.model.mapper.CustomerMapper;


@Service

public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	private CustomerMapper mapper;
	
	@Override
	public int addCustomer(Customer customer) {

		return mapper.addCustomer(customer);
	}

	
	
}
