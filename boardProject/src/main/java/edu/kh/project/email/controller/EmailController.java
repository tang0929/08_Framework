package edu.kh.project.email.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.project.email.model.service.EmailService;
import lombok.RequiredArgsConstructor;

@SessionAttributes({"authKey"}) // model값 session으로 변경함
@Controller
@RequestMapping("email")
@RequiredArgsConstructor // final필드에 자동으로 의존성 주입을 함. @Autowired 생성자 방식 코드 자동 완성
public class EmailController {
	
	
	// 필드에 의존성 주입하는 방법(권장 X)
//	@Autowired // 의존성 주입
 	//private EmailService service;
	
	
	/*
	 * @Autowired를 이용한 주입 방법은 3가지
	 * 
	 * 1) 필드
	 * 
	 * 2) setter
	 * 
	 * 3) 생성자 (권장)
	 * 
	 * Lombok 라이브러리(자주쓰는 코드 자동완성)에서 @RequiredArgsConstructor를 이용하면 
	 * 필드 중 
	 * 1) 초기화되지 않은 final이 붙은 필드
	 * 2) 초기화되지 않은 @notnull이 붙은 필드
	 * 
	 * 1),2)에 해당하는 필드에 대한 @Autowired 생성자 구문을 자동 완성
	 */
	 
	
	
	private final EmailService service;
	
	
	
	@ResponseBody
	@PostMapping("signup")
	public int signup(@RequestBody String email,Model model) {
	
		String authKey = service.sendEmail("signup",email);
		

		if(authKey != null) { // 인증번호가 반환돼서 돌아옴 (= 이메일이 성공적으로 보내짐)
			
			
			// 이메일로 전달한 인증번호를 Session에 올려둠
			model.addAttribute("authKey",authKey); 
			
			
			return 1;
		}
		
		
		return 0; // 이메일 전송 실패
		
	}
}
