package edu.kh.project.common.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BoardNameInterceptor implements HandlerInterceptor{

	
	// 후처리 방식인 postHandle을 사용해야함. Controller -> Dispatcher Servlet 사이
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		
		// application scope에서 boardTypeList 얻어오기
		ServletContext application = request.getServletContext();
		
		
		// 얻어온 boardTypeList는 getAttribute를 통해 확인해보면 Object형식이므로 이를 맞춰주기 위해 List로 다운캐스팅함
		List<Map<String, Object>> boardTypeList = 
		(List<Map<String, Object>>)application.getAttribute("boardTypeList");
		
		log.debug(boardTypeList.toString());
		
		
		/* uri : Uniform Resource Identifier 통합 자원 식별자
		 * url : 앞의 주소까지 포함시킴 (ex)localhost/board/1
		 * 자원 이름(주소)만 봐도 무엇인지 구별할 수 있는 문자열 */
		String uri = request.getRequestURI();
		
	
		int boardCode = Integer.parseInt(uri.split("/")[2]);
		// ["","board","1"] 형태로 쪼개지는데 이 중 2번 인덱스 값을 얻어옴
		
		
		
		// boardTypeList에서 boardCode를 하나씩 꺼내어 비교
		for(Map<String, Object> boardType : boardTypeList) {
			
			// 결과값이 object 자료형으로 출력됨. object를 String으로, String을 int로 변환할 예정
			// String.valueOf(값) : String으로 변환
			int temp = Integer.parseInt(String.valueOf(boardType.get("boardCode")));
			
			
			// 꺼낸 값과 boardCode값이 같을 경우, request scope에 boardName을 추가
			if(temp == boardCode) {
				request.setAttribute("boardName",boardType.get("boardName"));
				break;
			}
			
			
		
			
			
			
		}
		// log.debug("uri : " + uri);
		
		
		// boardTypeList 구성 : [{boardCode : 1, boardName = 공지}, {boardCode : 2, boardName = 자유}...]
		
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	

}
