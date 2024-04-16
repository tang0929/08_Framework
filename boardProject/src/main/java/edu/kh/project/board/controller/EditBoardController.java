package edu.kh.project.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.EditBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("editBoard")
@Slf4j
public class EditBoardController {
	
	
	private final EditBoardService service;
	
	
	
	
	/**
	 * 글쓰기 화면으로 이동
	 * @return board/boardWrite
	 */
	
	// /editBoard/${boardCode}/insert
	
	@GetMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(
			@PathVariable("boardCode") int boardCode) {
		
		
		return "board/boardWrite";
		
	}

	
	
	/** 게시글 작성
	 * 
	 * @param boardCode : 어떤 게시판에 작성한 글인지 구분
	 * @param inputBoard : 입력된 값(제목, 내용) + 추가 데이터 저장(커맨드 객체)
	 * @param loginMember : 로그인한 회원 번호 얻어오는 용도
	 * @param images : 제출된 file 타입 input 태그 데이터들
	 * @param ra : 리다이렉트 시 request scope로 데이터 전달
	 * 
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@PostMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(
		@PathVariable("boardCode") int boardCode,
		@ModelAttribute Board inputBoard,
		@SessionAttribute("loginMember") Member loginMember,
		@RequestParam("images") List<MultipartFile> images,
		RedirectAttributes ra) throws IllegalStateException, IOException 
	{
			
		
		/* ** 전달되는 파라미터 확인 **
		 * 
		 * List<MultipartFile> 
		 * - 5개 모두 업로드 => 0~4번 인덱스에 파일 저장됨
		 * - 5개 모두 업로드 안하면 => 0~4번 인덱스에 파일 저장 X
		 * - 2번 인덱스만(일부만) 업로드 => 2번 인덱스만 파일 저장, 0,1,3,4번 인덱스는 저장 안됨
		 * 
		 * 문제점 : 파일이 선택되지 않은 input 태그도 제출되고 있음. 제출은 됭ㅆ는데 데이터는 ""(빈칸)
		 *		파일이 선택이 안된 input 태그 값을 서버에 저장하려고 하면 오류 발생
		 *
		 * 해결 방법 : 무작정 서버에 저장하는게 아니라, 제출된 파일이 있는지 확인하는 로직을 추가 구성 
		 * 
		 * 
		 * + List요소의 index 번호 == IMG_ORDER 와 같음
		 * */
		
		
		// 1. boardCode, 로그인한 회원 번호를 inputBoard에 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		
		
		
		// 2. 서비스 메서드 호출 후 결과 반환 받기
		// 성공 시 [상세조회] 를 요청할 수 있도록 삽입된 
		int boardNo = service.boardInsert(inputBoard, images);
		
		
		
		// 3. 서비스 결과에 따라 message, redirect 경로 지정
		
		
		String path = null;
		String message = null;
		
		
		if(boardNo > 0) {
			// 성공시
			path = "/board/"+ boardCode + "/" + boardNo; // 상세 조회 경로
			message = "게시글이 작성되었습니다.";
		} else {
			
			// 실패시
			path = "insert";
			message = "게시글 작성 실패";
		}
	
		
		ra.addFlashAttribute("message",message);
		
		
		// insert 성공 시 작성된 글 상세 조회로 redirect
		return "redirect:" + path;
	}
	
	
	
	
	
	/**
	 * 게시글 삭제(=del_fl= Y로 변경하기)
	 * @param boardNo
	 * @param boardCode
	 * @param ra
	 * @return
	 */
	
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/delete")
	
	public String boardDelete(
			
			@PathVariable("boardNo") int boardNo,
			@PathVariable("boardCode") int boardCode,
			RedirectAttributes ra) {
		

		
		int result = service.boardDelete(boardNo, boardCode);
		
		
		String path = null;
		String message = null;
	
		
		
		if (result > 0) {
			
			message = "삭제되었습니다";
			
			path = "/board/" + boardCode;
		
			
		}
		else {
			
			message = "삭제 실패";
			
			path = "/board/" + boardCode + "/" + boardNo;
	
			
		}
		
		ra.addFlashAttribute("message",message);
		
		return "redirect:" + path;
	}
}

