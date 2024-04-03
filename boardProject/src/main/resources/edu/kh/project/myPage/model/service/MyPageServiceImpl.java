package edu.kh.project.myPage.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{
	
	private final MyPageMapper mapper;
	
	
// @RequiredArgsConstructor 를 이용했을 때 자동 완성 되는 구문
	//	@Autowired
	//	public MyPageServiceImpl(MyPageMapper mapper) {
	//		this.mapper = mapper;
	//	}
	
	
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {
		
		// 입력된 주소가 있을 경우 memberAddress를 A^^^B^^^C^^^ 형태로 가공함
		
		// 주소 입력이 안되어있으면 inputMember.getMemberAddress()는 ",,"형태로 나옴
		
		if(inputMember.getMemberAddress().equals(",,")) {
			
			// 주소 입력이 안되어있으면 null을 대입시킴
			inputMember.setMemberAddress(null);
		} else {
			
			String address = String.join("^^^", memberAddress);
			
			
			// ^^^가 붙은 inputMember를 세팅
			inputMember.setMemberAddress(address);
		}
		
		return mapper.updateInfo(inputMember);
	}
	
	
	
	// final 필드를 사용하는 대신 autowired할 필요 없음
	private final BCryptPasswordEncoder bcrypt;
	
	
	
	/**
	 * 현재 비밀번호가 맞는지 체크
	 * @param loginMember 
	 */
	@Override
	public int changePw(int memberNo ,Map<String, Object> paramMap) {
		
		
		// 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		
		String beforePw = mapper.selectPw(memberNo);
		
		
		// Map에 담긴 객체는 Object타입으로 인식되기 때문에 String 타입으로 강제 형변환시켜줘야함
		if(!bcrypt.matches((String)paramMap.get("currentPw"),beforePw)) {
			
			
			
			
			return 0;
			
			 
			
		} else {
			// 새 비밀번호 암호화
			String encPw = bcrypt.encode((String)paramMap.get("newPw"));
			
			// mapper에 전달 가능한 파라미터는 1개이므로 묶어서 전달해야함
			
			paramMap.put("encPw", encPw);
			paramMap.put("memberNo", memberNo);
			return mapper.changePw(paramMap);
			
		}
		
		
		
		
		 

		
	}
	
	
	
	
	
}
