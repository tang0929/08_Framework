package com.kh.test.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Student {
	
	private int studentNo;
	private String studentName;
	private String studentMajor;
	private String studentGender;
	
}
