package edu.kh.project.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.service.CommentService;
import lombok.RequiredArgsConstructor;

@RestController 
// @Controller(요청/응답 제어 + bean 등록 + @ResponseBody(응답 본문을 데이터로 반환
@RequiredArgsConstructor
@RequestMapping("comment")
public class CommentController {

	private final CommentService service;
	
	

	/**
	 * 댓글 목록 조회
	 * @param boardNo
	 * @return
	 */
	 // value속성 : 매핑할 주소 / produces 속성 : 응답할 데이터의 형식을 지정
	
	
	@GetMapping(value = "", produces = "application/json")
	public List<Comment> select(
			@RequestParam("boardNo") int boardNo){
		
		// HttpMessageConvert를 통해 JS(문자열)로 변환해서 응답
		
		return service.select(boardNo);
	}
	
	
	
	
	
	/**
	 * 댓글/답글 추가
	 * @param boardNo
	 * @return
	 */
	@PostMapping("")
	public int insert(
			@RequestBody Comment comment) {
		
		
		// 요청 데이터 JSON으로 명시됨
		// headers : {"Content-Type" : "application/json"}
		
		// -> Arguments Resolver가 JSON을 DTO로 자동 변경해줌
		// JACKSON LIBRARY 가닝
		return service.insert(comment);
	}
	
	
	
	
	

	/**
	 * 댓글 수정
	 * @param comment (번호, 내용)
	 * @return result
	 */
	@PutMapping("")
	public int update(
			@RequestBody Comment comment) {
		
		
		
		return service.update(comment);
	}
	
	
	
	
	
	/**
	 * 댓글 삭제
	 * @param commentNo
	 * @return result
	 */
	@DeleteMapping
	public int delete(
			@RequestBody int commentNo) {
		
		return service.delete(commentNo);
	}
	
	
	
}
