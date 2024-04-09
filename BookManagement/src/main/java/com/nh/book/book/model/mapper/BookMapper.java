package com.nh.book.book.model.mapper;

import java.awt.print.Book;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper {

	

	List<Book> selectMemberList();

}
