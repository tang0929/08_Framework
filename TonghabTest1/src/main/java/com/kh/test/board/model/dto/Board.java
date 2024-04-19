package com.kh.test.board.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Board {

	private int userNo;
	private String userId;
	private String userName;
	private int userAge;
	
	
	private int boardNo;
	private String boardTitle;
	private String boardContent;
	private String boardDate;
	private int boardReadcount;
	
}
