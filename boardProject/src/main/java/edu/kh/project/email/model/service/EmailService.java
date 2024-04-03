package edu.kh.project.email.model.service;

import java.util.Map;

public interface EmailService {

	
	
	/**
	 * 이메일 전송
	 * @param string
	 * @param email
	 * @return authkey
	 */
	String sendEmail(String string, String email);

	
	/**
	 * 이메일,인증번호 입력받고 일치여부 확인
	 * @param map
	 * @return
	 */
	int checkAuthKey(Map<String, Object> map);

}
