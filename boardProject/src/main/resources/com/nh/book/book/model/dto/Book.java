package com.nh.book.book.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Book {
	
	private int bookNo;
	private String bookTitle;
	private String bookWriter;
	private String bookPrice;
	private String regDate;

}
