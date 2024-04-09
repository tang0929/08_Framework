package com.nh.book.book.model.service;

import java.awt.print.Book;
import java.util.List;

public interface BookService {

	int enroll(Book book);

	List<Book> selectBookList();

}
