package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// instance : 개발자가 만들고 관리하는 객체

// Bean : 스프링이 만들고 관리하는 객체

@Controller // 요청/응답 제어 역할 명시 + Bean 등록

public class ExampleController{
	
	/* 요청 주소 매핑하는 방법
	 * 
	 * 1) @RequestMapping("주소")
	 * 
	 * 2) @GetMapping("주소") : GET 방식 요청 매핑
	 * 	  @PostMapping("주소") : POST 방식 요청 매핑
	 *    @PutMapping("주소") PUT(수정) 방식 요청 매핑(form, js, a태그 요청 불가)
	 *    @DeleteMapping("주소") : DELETE(삭제) 방식 요청 매핑(form, js, a태그 요청 불가)
	 * 
	 */

	@GetMapping("example") 
	public String exampleMethod() {
		
		System.out.println("정적자원 페이지입니다.");
		
		/* forward 하려는 html 파일 경로를 작성. 단, ViewResolver가 제공하는
		* Thymele 접두사, 접미사 제외하고 작성 
		* 
		* 접두사 : classpath:/templates
		* 접미사 : .html */
		
		return "example";
	}
	
}
