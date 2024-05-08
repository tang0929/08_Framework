package edu.kh.project.common.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Scheduler : 스프링에서 제공하는 일정 시간/주기 마다 예정된 코드를 실행하는 객체
 * 
 * [설정 방법]
 * 1. 프로젝트명Application.java 파일에 @EnableScheduling 어노테이션 추가
 * 2. 스케쥴러 코드를 작성할 별도 클래스를 생성한 후 Bean으로 등록
 * 	-> @Component 어노테이션 작성
 * 3. 해당 클래스에 @Scheduled(시간/주기) 어노테이션을 추가한 메서드 작성
 * 	-> 주의사항 : 해당 메서드는 반환형이 존재해서는 안된다 => void 사용해야 함 
 */


@Component // bean으로 등록 -> 스프링이 자동으로 스케쥴링 코드를 수행하게 함
@Slf4j
public class TestScheduling {
	
	
	/**
	 * @Scheduled() 매개변수
	 *
	 * fixedDelay : 이전 작업이 끝난 후 다음 작업 시작할 때 까지의 시간을 설정 
	 * fixedRate : 이전 작업이 시작하면 다음 작업 시작할 때 까지의 시간을 설정 
	 * cron : UNIX 계열 잡 스케쥴러 표현식, cron = "초 분 시 일 월 요일 [년도]", 일요일 1 ~ 토요일 7
	 * 		ex) 2024년 5월 7일 화요일 12시 50분 0초에 수행 
	 * 			cron = "0 50 12 7 5 3 2024"
	 * 			년도 부분은 생략 가능
	 * 		
	 * * : 모든 수
	 * - : 두 수 사이의 값 ex) 10-15 : 10 11 12 13 14 15
	 * , : 특정 값을 지정  ex) 분 자리에 3,6,9,12 : 3분 6분 9분 12분에 동작
	 * / : 값 증가 	   ex) 0/5 : 0부터 시작해서 5씩 증가할 때 마다 수행		
	 * ? : 특별한 값이 없음(월/요일만 가능)
	 * L : 마지막(월/요일만 가능)
	 */
	
	
	
	
	// @Scheduled(fixedDelay = 5000) // ms단위, 5초마다 console에 출력
	// @Scheduled(fixed Rate = 5000) 
	// @Scheduled(cron = "0/10 * * * * * ") // 0초 시점을 기준으로 10초마다 실행 
	// @Scheduled(cron = "0 0 0 * * *") // 매일 자정마다 수행
	public void testMethod() {
		
		log.info("스케쥴러 테스트 중");
		
		
	}

}
