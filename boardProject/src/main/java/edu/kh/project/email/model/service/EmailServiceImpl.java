package edu.kh.project.email.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import edu.kh.project.email.model.mapper.EmailMapper;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;


// @Transactional : 예외가 발생하면 롤백(기본값 commit)
@Service // Bean 등록
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
	
	
	// EmailConfig 설정이 적용된 객체(메일 송신 가능)
	private final JavaMailSender mailSender;
	
	
	
	// 타임리프(템플릿 엔진)를 이용해서 html코드를 JAVA로 변환함
	private final SpringTemplateEngine templateEngine;
	
	
	// Mapper 의존성 주입
	private final EmailMapper mapper;
	
	
	/**
	 * 이메일 전송
	 */
	@Override
	public String sendEmail(String htmlName, String email) {
		
		// 6자리 난수(인증코드) 생성(밑에 있음)
		String authKey = createAuthKey();
		
		try {
			
			// 제목
			String subject = null;
			
			
			switch(htmlName) {
			
			case "signup" : subject = "[boardProject] 회원 가입 인증번호입니다."; break;
			}
			
			
			
			// 인증 메일 보내기
			// MimeMessage : Java에서 메일을 보내는 객체
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			
			// MimeMessageHelper : Spring에서 제공하는 메일 발송 도우미(간단 + 타임리프)
			// 2번 매개변수 true : 파일 전송을 할건지? true/false 여부 결정
			// 3번 매개변수 UTF-8 : 문자 인코딩 지정
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			
			
			helper.setTo(email); // 이메일을 받는 사람 지정
			helper.setSubject(subject); // 이메일의 제목 지정
			helper.setText(loadHtml(authKey,htmlName), true); // ()안에 타임리프가 적용된 html + HTML 코드 해석 여부를 허용
			
			
			// CID(content-ID)를 이용해 메일에 이미지 첨부(=/ 파일첨부, 이메일 내용에 사용할 이미지를 첨부하는 것)
			// logo.jpg를 메일 내용에 첨부하는데 사용하고 싶으면 "logo"라는 id를 작성해야함
			helper.addInline("logo",new ClassPathResource("static/images/logo.jpg"));
			
			
			// 메일 보내기
			mailSender.send(mimeMessage);
			
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		// 이메일 + 인증번호를 tb_auth_key 테이블에 저장
		Map<String, String> map = new HashMap<>();
		map.put("authKey",authKey);
		map.put("email",email);
		
		// 1) 해당 이메일이 DB에 존재하는 경우가 있을 수 있기 때문에 수정(update)먼저 진행
		// 1 반환시 업데이트 성공
		
		int result= mapper.updateAuthKey(map);
		
		
		// 1번 update 실패치 insert시도
		if(result == 0 ) {
			result = mapper.insertAuthKey(map);
		}
		// 수정/삭제 후에도 result = -0
		if (result ==0) return null;
		
		
		
		// 오류 안나고 제대로 이메일 발송되면 생성된 authKey를 보내줌.
		return authKey;
	}
	
	
	// HTML 파일 읽어와 String으로 변환(타임리프)
	public String loadHtml(String authKey, String htmlName) {
		
		// 타임리프가 포함된 context를 import함
		Context context = new Context();
		
		
		// model.attribute("authKey",authKey)처럼 타임리프 적용된 html에서 사용할 값 추가
		
	
		context.setVariable("authKey",authKey);
		
		
		// templates/email 폴더에서 htmlName과 같은 .html 파일을 찾아서 .html 파일 내용을 읽어와 String으로 변환시킴
		return templateEngine.process("email/" + htmlName,context);
		
	}
	
	
	
	
	
	
	/**
	 * 인증번호 생성 (영어 대문자 + 소문자 + 숫자 6자리)
	 * 
	 * @return authKey
	 */
	public String createAuthKey() {
		String key = "";
		for (int i = 0; i < 6; i++) {

			int sel1 = (int) (Math.random() * 3); // 0:숫자 / 1,2:영어

			if (sel1 == 0) {

				int num = (int) (Math.random() * 10); // 0~9
				key += num;

			} else {

				char ch = (char) (Math.random() * 26 + 65); // A~Z

				int sel2 = (int) (Math.random() * 2); // 0:소문자 / 1:대문자

				if (sel2 == 0) {
					ch = (char) (ch + ('a' - 'A')); // 대문자로 변경
				}

				key += ch;
			}

		}
		return key;
	}
	
	
	
	
	
	/**
	 * 이메일, 인증번호 일치 여부 확인
	 */
	@Override
	public int checkAuthKey(Map<String, Object> map) {
	
		
		return mapper.checkAuthKey(map);
	}

}


	/*
	 * Google SMTP를 이용한 이메일 전송하기
	 * 
	 * - SMTP(Simple Mail Transfer Protocol, 간단한 메일 전송 규약)
	 * -> 이메일 메시지를 송수신할 때 사용하는 규약, 방법
	 * 
	 * - Google SMTP
	 * 
	 * Java Mail Sender -> Google SMTP -> 대상에게 이메일 전송
	 * 
	 * Java Mail Sender에 Google SMTP 이용 설정을 추가함
	 * 
	 * 1) config.properties 내용 추가
	 * 2) EmailConfig.java 
	 * 
	 * 
	 */