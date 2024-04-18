package edu.kh.project.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import edu.kh.project.board.model.service.BoardService;
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
	private final BoardService boardService;
	
	
	
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
		 * 문제점 : 파일이 선택되지 않은 input 태그도 제출되고 있음. 제출은 됐는데 데이터는 ""(빈칸)
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
	
	
	
	
	
	/**
	 * 게시글 수정 화면 전환
	 * @param boardCode : 게시판 종류
	 * @param boardNo : 게시글 번호
	 * @param loginMember : 로그인 회원(자신이 쓴 글만 수정할 수 있게 설정하기 위한 용도)
	 * @param model : request scope로 값 전달
	 * @param ra
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(
			@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			@SessionAttribute("loginMember") Member loginMember,
			Model model,
			RedirectAttributes ra) {
		
		
		// 수정 화면에 들어가면 기본적으로 기존에 있던 제목/내용/이미지가 작성된 상태로 조회됨.
		
		
		Map<String, Integer> map = new HashMap<>();
		
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		
		
		// 게시글 상세조회하는 BoardService.selectOne(map)을 호출
		// 맨 위에 BoardService 의존성 주입 코드 작성하기
		Board board = boardService.selectOne(map);
		
		
		String path = null;
		String message = null;
		
		
		if(board == null) {
			// 수정화면 진입 실패
			message = "해당 게시글이 존재하지 않습니다.";
			path = "redirect:/";
			
			ra.addFlashAttribute("message",message);
			
		} else if(board.getMemberNo() != loginMember.getMemberNo()) {
			// 자신이 작성한 글이 아닐때
			
			message = "자신이 작성한 게시글만 수정할 수 있습니다.";
			

			
			// 상세 조회 페이지로 날려보냄
			path = String.format("redirect:/board/%d/%d", boardCode, boardNo);
					
			ra.addFlashAttribute("message",message);
		}
		else {
			
			// 정상적인 수정 화면 진입
			
			path = "board/boardUpdate";
			
			model.addAttribute("board",board);
		}
		
		return path;
	}
	
	
	
	
	
	
	/**
	 * 게시글 수정
	 * @param boardCode : 게시판 종류
	 * @param boardNo : 수정할 게시글 번호
	 * @param inputBoard : 커멘드 객체(제목 내용)
	 * @param loginMember : 현재 로그인한 사람(회원 번호)
	 * @param images : input type = "file" 다 얻어오기
	 * @param ra : redirect시 request scope 값 전달 
	 * @param deleteOrder : 삭제된 이미지 순서가 기록된 문자열
	 * @param querystring : 수정 성공 시 이전 파라미터 유지(cp, 검색어)
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(
			@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			@ModelAttribute Board inputBoard,
			@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("images") List<MultipartFile> images,
			RedirectAttributes ra,
			@RequestParam(value = "deleteOrder", required = false) String deleteOrder,
			@RequestParam(value = "querystring", required = false, defaultValue = "") String querystring) throws IllegalStateException, IOException 
	{ 
		
		
		// 1. 커멘드 객체 inputBoard에 boardCode, boardNo, memberNo를 세팅
		
		inputBoard.setBoardCode(boardCode);
		inputBoard.setBoardNo(boardNo);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		
		// -> inputBoard에는 제목, 내용, boardCode, boardNo, memberNo)
		
		
		
		
		// 2. 게시글 수정 서비스 호출 후 결과가 반환받기
		
		int result = service.boardUpdate(inputBoard,images,deleteOrder);
		
		
		
		// 3. 서비스 결과에 따라 응답 제어
		
		String message = null;
		String path = null;
		
		
		if(result > 0) {
			
			message = "게시글이 수정되었습니다.";
			
			path = String.format("/board/%d/%d%s", boardCode, boardNo, querystring);
			
		} else {
			
			message = "게시글 수정 실패";
			
			path = "update";  // 수정화면 전환 상대 경로
		}
		
		ra.addFlashAttribute("message",message);
		return "redirect:" + path;
		
		/**
		 * 게시글 수정 시 이미지 부분에 생각해야하는 것
		 * 
		 * 1) 기존에 이미지가 있었는데, 삭제할 경우 (deleteOrder) 
		 *  -> DELETE 구분 수행
		 *  
		 *  DELETE FROM "BOARD_IMG" WHERE IMG_ORDER IN (${deleteOrder}) AND BOARD_NO = #{boardNo}
		 *  
		 *  
		 * 2) 기존에 이미지가 있고 새로 변경되었을 경우
		 *  -> UPDATE 구문 수행
		 *  
		 *  
		 *  
		 * 3) 아무것도 없었다가 새 이미지를 추가할 경우
		 *  -> INSERT 수행
		 * 
		 * 
		 * 
		 * 
		 */
		
	}
}

