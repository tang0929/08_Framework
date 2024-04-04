package edu.kh.project.main.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.project.main.model.mapper.MainMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService{

	private final MainMapper mapper;
	private final BCryptPasswordEncoder bcrypt;
	
	
	/**
	 * 비밀번호 초기화
	 */
	@Override
	public int resetPw(int inputNo) {
		
		// 초기화할 비밀번호를 지정함
		String pw = "test01";
		
		String encPw = bcrypt.encode(pw);
		
		// 회원번호와 암호화된 비밀번호를 같이 묶음
		
		// Object는 모든 클래스의 최상위. 
		Map<String, Object> map = new HashMap<>();
		map.put("inputNo", inputNo);
		map.put("encPw", encPw);
		
		
		
		return mapper.resetPw(map);
	}

	
	
	
	/**
	 * 탈퇴 복구
	 */
	@Override
	public int resetSecession(int inputNo2) {
		
		
		
		return mapper.resetSecession(inputNo2);
	}
}
