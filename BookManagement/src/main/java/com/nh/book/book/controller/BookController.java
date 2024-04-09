package com.nh.book.book.controller;

import java.awt.print.Book;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nh.book.book.model.service.BookService;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@RequestMapping("book")
public class BookController {
	
  	@Autowired
  	public BookService service;
	
	@GetMapping("enroll")
	public String enrollPage() {
		return "book/enroll";
	}


	@GetMapping("detail")
	public String detailPage() {
		return "book/detail";
	}

	
	
	@PostMapping("enroll")
	public String enroll(Book book, RedirectAttributes ra) {
		
		
		int result = service.enroll(book);
		
		return null;
	}
		
	
		
		

	@ResponseBody
	@GetMapping("selectBookList")
	public List<Book> selectBookList(){
		
		List<Book> bookList = service.selectBookList();
		
		return bookList;
	}
		
	
		
		
	
	
	
	
	
	
	
	
	
	
	

}
