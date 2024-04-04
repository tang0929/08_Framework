package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/*
 * Filter : 요청, 응답 시 걸러내거나 추가할 수 있는 객체
 * 
 * [필터 클래스 생성 방법]
 * 1. jakarta.servlet.Filter 인터페이스를 상속받기
 * 2. doFilter() 메서드를 오버라이딩
 */




// 로그인이 되어있지 않은 경우 특정 페이지로 돌아가게 함
public class LoginFilter implements Filter{

	
	
	// 필터 동작을 정의하는 메서드
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		// ServletRequest : HttpServletRequest의 부모 타입
		// ServletResponse : HttpServletResponse의 부모 타입
	
		
		// 다운캐스팅을 해야 사용가능. 다운캐스팅을 해야 Http 통신이 가능하게됨
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		
		// Session 얻어오기
		HttpSession session = req.getSession();
		
		
		// 세션에서 로그인한 정보를 얻어왔는데 없을때(=로그인 안함)
		if(session.getAttribute("loginMember") == null) {
			
			// resp 객체로 /loginError로 보내버리기
			resp.sendRedirect("/loginError");
			
		}else {
			// FilterChain : 다음 필터 또는 Dispatcher Servlet과 연결된 객체
			// 로그인이 되어있었을 경우
			// 다음 필터로 요청/응답 객체를 전달.
			// 만약 더 이상의 필터가 없으면 Dispatcher Servlet으로 전달
			chain.doFilter(request, response);
		}
		
	}
}
