package edu.kh.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;

@Controller // Bean 등록
@RequestMapping("todo") // /todo로 시작하는 요청 매핑
public class TodoController {
	
	@Autowired // 같은 타입 Bean 의존성 주입(DI)
	private TodoService service;
	
	
	@PostMapping("add") // "/todo/add Post 방식을 매핑"
	public String addTodo
	(@RequestParam("todoTitle") String todoTitle,
	@RequestParam("todoContent") String todoContent,
	RedirectAttributes ra)
	{
		
		
		
		// RedirectAttributes : 리다이렉트 시 값을 1회성으로 전달하는 객체
		
		// RedirectAttributes.addFlashAttribute("key", value) 형식으로 잠깐 세션에 속성 추가
		
		// [원리]
		// 응답 전 : request scope
		// redirect 중 : session scope로 이동
		// 응답 후 : reqeust scope로 복귀
		
		
		
		
		
		// 서비스 호출 후 결과 반환 받기
		int result = service.addTodo(todoTitle, todoContent);
		
		
		// 삽입 결과에 따라 message 값 지정
		String message = null;
		
		if (result > 0) message = "할 일 추가 성공";
		else message = "할 일 추가 실패";
		
		// redirect 후 1회성으로 사용할 데이터를 속성으로 추가
		ra.addFlashAttribute("message" , message);
		
		return "redirect:/";  // 메인페이지 재요청
	}
	
	
	
	// 상세 조회
	@GetMapping("detail")
	public String todoDetail
	(@RequestParam("todoNo") int todoNo, Model model, RedirectAttributes ra) {
		
		Todo todo = service.todoDetail(todoNo);
		
		String path = null;
		
		if (todo != null ) {
			// 조회 결과가 있으면
			path = "/todo/detail"; // forward 경로. templates/todo/detail.html 경로임
			
			// request scope 값 세팅
			model.addAttribute("todo",todo);
		} else {
			// 조회 결과가 없으면
			path = "redirect:/";  // 메인 페이지로 리다이렉트 재요청
			
			// RedirectAttribues : 리다이렉트시 데이터를 requestscope로 전달할 수 있는 객체
			ra.addFlashAttribute("message","내용이 존재하지 않습니다.");
		}
		
		return path;
	}
	
	
	// 할 일 삭제
	
	/**
	 * 
	 * @param todoNo : 삭제 할 일 번호
	 * @param ra : 리다이렉트시 1회성으로 데이터를 전달하는 객체
	 * @return 메인페이지/상세페이지로
	 */
	
	/*
	 * /todo/delete Get 방식 요청
	 * 파라미터 : todoNo(int)
	 * 
	 * 삭제 Mapper 호출
	 * Delete 구문 수행 후 결과 반환(int)
	 * Service에서 트랜잭션
	 * 
	 * Controller로 돌아와 성공시 삭제성공 실패치 삭제 실패 메시지
	 */
	@GetMapping("delete")
	public String todoDelete 
	(@RequestParam("todoNo") int todoNo,RedirectAttributes ra) {
		
		int result = service.deleteTodo(todoNo);
		
		String message = null;
		String path = null;
		
		if (result > 0) {
			ra.addFlashAttribute("message","삭제 성공");
			
			}
		else { 
			path = "/todo/detail?todoNo="+todoNo;
			ra.addFlashAttribute("message","삭제 실패");
		}
		
		return "redirect:/";
	}

	
	// 할 일 수정
	/**
	 * 
	 * @param todoNo : 수정할 할 일 번호
	 * @param model : 데이터 전달 객체 (기본 request)
	 * @return todo/update
	 */
	// 수정 화면으로 전환
	@GetMapping("update")
	public String todoUpdate
	(@RequestParam("todoNo") int todoNo, Model model) {
		
		// 일단 상세조회 서비스 호출. 수정화면에 출력할 수정하기 이전 내용
		Todo todo = service.todoDetail(todoNo);
		
		model.addAttribute("todo",todo);
		
		return "todo/update";
	}
	
	
	
	// 수정 내용을 제출
	/**
	 * 
	 * @param updateTodo : 커맨드 객체(전달받은 파라미터가 자동으로 필드에 세팅된 객체)
	 * @return
	 */
	@PostMapping("update") 
	public String todoUpdate
	(@ModelAttribute Todo todo, RedirectAttributes ra) {
		
		/*
		 * @ModelAttribute(생략 가능)
		 * 한꺼번에 requestParam을 처리할 수 있음
		 */
		
		
		// 수정 서비스 호출
		int result = service.todoUpdate(todo);
		
		String message = null;
		String path = "redirect:"; // 미리 리다이렉트 페이지를 설정함
	
		
		if(result > 0) {
			// 수정된 내용을 다시 볼 수 있게 수정 후 상세 조회로 리다이렉트함
			path += "/todo/detail?todoNo="+todo.getTodoNo();
			message = "수정 완료";
		} else {
			// 수정화면으로 다시 리다이렉트
			path += "/todo/update?todoNo="+todo.getTodoNo();
			message = "수정 실패";
			}
		
		ra.addFlashAttribute("message",message);
		return path;
	}
	
	
	
	
	/**
	 * 완료 여부 바꾸기
	 * @param todo : 커멘드 객체 (@ModelAttribute 생략) todoNo, complete 두 필드가 세팅됨
	 * @param ra : 
	 * @return "detail?todoNo="+todoNo (상대 경로)
	 */
	@GetMapping("changeComplete")
	public String changeComplete
	(Todo todo, RedirectAttributes ra) {
		
		// 변경 서비스 호출
		int result = service.changeComplete(todo);
		
		String message = null;
		
		
		if (result > 0) {
			 
			message = "완료 여부 변경 성공";
		} else
			 
			message = "완료 여부 변경 실패";
		
		
		// 현재 요청 주소 : /todo/changeComplete
		// 응답 주소 : /todo/detail
		ra.addFlashAttribute("message",message);
		return "redirect:detail?todoNo="+todo.getTodoNo();
		
	}
	
	
	
}
