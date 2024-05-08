package edu.kh.project.common.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Component
@Aspect
@Slf4j
public class LoggingAspect {

	
	/**
	 * 컨트롤러 수행 전 로그 출력 
	 * @param jp
	 */
	@Before("PointcutBundle.controllerPointCut()")
	public void beforeController(JoinPoint jp) {
		
		String className = jp.getTarget().getClass().getSimpleName();
		
		String methodName =  jp.getSignature().getName() + "()";
		
		// 요청한 클라잉언트의 HttpServletRequest 객체 얻어오기
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		// 클라이언트 ip 얻어오기
		String ip = getRemoteAddr(req);
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("\n[%s.%s] 요청\n", className, methodName));
		sb.append(String.format("ip : %s", ip));
		
		
		
		// 로그인 상태라면
		if(req.getSession().getAttribute("loginMember") != null) {
			
			// 로그인한 멤버를 Member로 형변환 한 후 email만 얻어온다.
			String memberEmail = 
					((Member)req.getSession().getAttribute("loginMember")).getMemberEmail();
			
			sb.append(String.format(", 요청 회원 : %s", memberEmail));
		}
		
		log.info(sb.toString());
	}
			
	
	// --------------------------------------------------------------------------
	
	
	
	/* ProceedingJoinPoint
	 * - JoinPoint를 상속한 자식 객체
	 * - @Around에서 사용 가능
	 * 주의점 : @Around 사용 시 반환형으로 Object가 나옴
	 * @Around 메서드 종료시 proceed 반환값을 return해야됨
	 */
	
	/*
	 * proceed() 메서드 제공 -> proceed 메서드 호출 전/후로 Before/After가 구분
	 */
	
	/**
	 * 서비스 수행 전/후 동작하는 코드(advice)
	 * @return pjp
	 * @return
	 */
	@Around("PointcutBundle.serviceImplPointCut()")
	public Object aroundServiceImpl(ProceedingJoinPoint pjp) throws Throwable{
		
		
		// @Before 부분
		// 클래스명
		String className = pjp.getTarget().getClass().getSimpleName();
	
		// 메서드명
		String methodName = pjp.getSignature().getName() + "()";
		
		
		log.info("========= {}.{} 서비스 호출 =========", className, methodName);
		
		
		// 파라미터
		log.info("Parameter : {}",Arrays.toString(pjp.getArgs()));
		
		
		// 서비스 코드 실행 시 시간을 기록
		
		// 컴퓨터 시간 기준으로 1970년 1월 1일 오전 9시 기준으로 지난 날짜를 보여줌
		long startMs = System.currentTimeMillis();
		
		
		
		Object obj = pjp.proceed();    // 전/후를 나누는 기준점
		
		
		
		// @After 부분
		
		long endMs = System.currentTimeMillis();
		
		log.info("Running Time : {}ms",endMs - startMs);
		
		log.info("============================================");
		
		return obj;
		
	}
	
	//====================================================================
	
	// 예외 발생 후 수행되는 코드
	// 사용 조건 : 서비스 메서드 별로 @Transaction이 작성되어있어야 동작함
	/**
	 * @Transaction 어노테이션 롤백 동작 후 수행
	 * @param jp
	 * @param ex
	 */
	@AfterThrowing(pointcut = "@annotation(org.springframework.transaction.annotation.Transactional)", throwing = "ex")
	public void transcationRollBack(JoinPoint jp, Throwable ex) {
		
		log.warn("***트랜젝션이 롤백*****",jp.getSignature());
		
		log.error("[롤백 원인] : {}", ex.getMessage());
		
	}
	
	
	
	/**
	 * 접속자 IP를 얻어오는 ㅔㅁ서버ㅡ
	 * 
	 * @param request
	 * @return
	 */
	private String getRemoteAddr(HttpServletRequest request) {

		String ip = null;

		ip = request.getHeader("X-Forwarded-For");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-RealIP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

}


