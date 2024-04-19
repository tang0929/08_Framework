package com.kh.test.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.test.board.model.dto.Board;
import com.kh.test.board.model.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	
	public final BoardService service;
	
	
	@GetMapping("select")
	public String select(
			Model model,
			@RequestParam("boardTitle") String boardTitle) {
		
		List<Board> boardList = service.selectBoardTitle(boardTitle);
		
		
		String path = null;
		
		if(boardList.isEmpty()) {
			
			path = "searchFail";
		}
		else {
			
			path = "searchSuccess";
			
			model.addAttribute("boardList", boardList);
		}
		
		
		return path;
	}
	
	
	
	
	
	

}
