package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

@Configuration
@PropertySource("classpath:/config.properties")
// config.properties에 작성된 내용을 해당 클래스에서 사용하겠다
public class FileConfig implements WebMvcConfigurer{
	
	
	// config.properties에 작성된 파일 업로드 임계값 얻어와 필드에 대입
	@Value("${spring.servlet.multipart.file-size-threshold}")
	private long fileSizeThreshold;
	
	@Value("${spring.servlet.multipart.max-request-size}")
	private long maxRequestSize; // 요청당 파일 최대 크기
	
	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxFileSize; // 개별 파일당 최대 크기
	
	@Value("${spring.servlet.multipart.location}")
	private String location; // 임계값 초과 시 임시 저장 폴더 경로
	
	@Value("${my.profile.resource-handler}")
	private String profileResourceHandler;
	
	@Value("${my.profile.resource-location}")
	private String profileResourceLocation;
	

	
	
	
	
	
	// 요청 주소에 따라 서버 컴퓨터의 어떤 경로에 접근할지 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry
		.addResourceHandler("/myPage/file/**")  // 클라이언트 요청 주소 패턴
		.addResourceLocations("file:///C:\\uploadFiles\\test\\");
		
		
		// 프로필 이미지 요청 - 서버 폴더 연결 추가
		registry
		.addResourceHandler(profileResourceHandler)  // /myPage/Profile
		.addResourceLocations(profileResourceLocation);
		
		
	}
	
	/*
	 * MultipartResolver 설정
	 */
	@Bean
	public MultipartConfigElement configElement() {
		
		// 객체 생성
		MultipartConfigFactory factory = new MultipartConfigFactory();
	
		
		
		factory.setFileSizeThreshold(DataSize.ofBytes(fileSizeThreshold));
		// fileSizeThreshold의 Data를 Bytes 사이즈로 지정
		
		
		factory.setMaxFileSize(DataSize.ofBytes(maxFileSize));
		
		
		factory.setMaxRequestSize(DataSize.ofBytes(maxRequestSize));
		
		
		factory.setLocation(location);
		
		
		return factory.createMultipartConfig();
	}
	
	
	// MultipartResolver 객체를 bean으로 추가.
	// 추가후 위에서 만든 MultipartConfig를 자동으로 이용
	// 파일이나 문자열을 요청 -> MultipartResolver을 통해 문자열은 String, 파일은 MultipartFile로 변환해서 저장
	@Bean
	public MultipartResolver multipartResolver() {
		
		StandardServletMultipartResolver multipartResolver 
		= new StandardServletMultipartResolver();
		
		
		return multipartResolver;
	}
	
	
	
	
}
