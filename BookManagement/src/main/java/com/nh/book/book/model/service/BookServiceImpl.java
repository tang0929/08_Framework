package com.nh.book.book.model.service;

import java.awt.print.Book;
import java.util.List;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.nh.book.book.model.mapper.BookMapper;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class BookServiceImpl implements BookService{
	
	private final BookMapper mapper;
	

	
	@Override
	public int enroll(Book book) {
		
	
		
		return 0;
	}
	
	
	
	@Override
	public List<Book> selectBookList() {
	
		
		return mapper.selectMemberList();
	}
}
