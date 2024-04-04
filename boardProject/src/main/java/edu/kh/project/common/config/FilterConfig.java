package edu.kh.project.common.config;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.kh.project.common.filter.LoginFilter;
import jakarta.servlet.FilterRegistration;

/*
 * 만들어놓은 Filter 클래스를 언제 적용될지 설정함 (LoginFilter)
 */



@Configuration // 서버가 켜질때 해당 클래스 내 모든 메서드가 실행됨
public class FilterConfig {
	
	
	@Bean // 반환된 객체를 등록
	public FilterRegistrationBean<LoginFilter> loginFilter(){
		
		// FilterRegistrationBean : 필터를 Bean으로 등록하게 하는 객체
		
		
		// filter 객체 생성
		FilterRegistrationBean<LoginFilter> filter = new FilterRegistrationBean<>();
		
		
		// LoginFilter라는 필터 객체를 추가함
		filter.setFilter(new LoginFilter());
		
		
		// myPage로 시작하는 모든 요청
		String[] filteringUrl = {"/myPage/*"};
		
		
		// 필터가 동작하는 URL들을 세팅
		// Arrays.asList() -> () 배열을 List로 변환함
		filter.setUrlPatterns(Arrays.asList(filteringUrl));
		
		
		
		// 필터의 이름을 지정함
		filter.setName("loginFilter");
		
		
		// 필터 순서 지정
		// 어떤 필터부터 실행할지 정함
		filter.setOrder(1);
		return filter ; // 반환된 객체가 필터를 생성해서 Bean으로 등록됨
	}
	
	

}
