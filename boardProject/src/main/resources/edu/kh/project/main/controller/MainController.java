package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.service.annotation.PutExchange;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.main.model.service.MainService;
import lombok.RequiredArgsConstructor;

@Controller // bean 등록
@RequiredArgsConstructor
public class MainController {
	
	private final MainService service;
	
	@RequestMapping("/") // "/" 요청을 매핑(method 가리지 않음)
	public String mainPage() {
		
		return "common/main";
	}
	
	
	/**
	 * 비밀번호 초기화
	 * @param inputNo : 초기화시킬 회원번호
	 * @return result
	 */
	
	@ResponseBody
	@PutMapping("resetPw")
	public int resetPw(@RequestBody int inputNo) {
		// body에서 inputNo를 받아오기 때문에 @RequestBody를 사용
		
		
		
		return service.resetPw(inputNo);
	}

	
	/**
	 * 탈퇴 복구
	 * @param inputNo2
	 * @return return
	 */
	@ResponseBody
	@PutMapping("resetSecession")
	public int resetSecession(@RequestBody int inputNo2) {
		
		return service.resetSecession(inputNo2);
	}
	
	
	
	
	/**
	 * loginFilter에서 리다이렉트하면 여기로 옴
	 * message로 로그인이 안됐다고 알린 후 메인 페이지로 redirect 시킴
	 * @return
	 */
	@GetMapping("loginError")
	public String loginError(RedirectAttributes ra) {
		ra.addFlashAttribute("message","로그인 후 이용할 수 있는 페이지입니다.");
		return "redirect:/";
	}
	
	
	
	
}
