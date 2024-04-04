package edu.kh.project.myPage.model.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	public int changePw(Map<String, Object> paramMap, int memberNo) {
		
		
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
	
	

	

		
		
	
	@Override
	public int secession(String memberPw, int memberNo) {
		
		// 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회
		String inputPw = mapper.selectPw(memberNo);
		if(!bcrypt.matches(memberPw,inputPw)) {
			return 0;
			
		} else
		
		return mapper.secession(memberNo);
	}
	
	
	
	/**
	 * 파일 업로드 테스트
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@Override
	public String fileUpload1(MultipartFile uploadFile) throws IllegalStateException, IOException {
		
		
		// MultipartFile이 제공하는 메서드
		// - getSize() : 파일크기
		// - isEmpty() : 업로드한 파일이 없으면 true
		// - getOriginalFileName() : 원본 파일명
		// - transferTo(경로) : 메모리 또는 임시 저장 경로에 업로드된 파일을 원하는 경로에 전송(서버 어떤 폴더에 지정하고 저장)
		
		
		if(uploadFile.isEmpty()) {
			
			// 업로드한 파일이 없으면 아무것도 실행하지 않음
			return null;
			
			
		} 
			// 업로드한 파일이 있을 경우 
			// uploadfiles의 test의 (파일명) 으로 서버에 저장
			uploadFile.transferTo(
					new File("C:\\uploadFiles\\test\\" + uploadFile.getOriginalFilename()));
			
			// 웹에서 해당 파일에 접근할 수 있는 경로를 반환
			
			// 서버 : C:\\uploadFiles\\test\\a.jpg
			// 웹 접근 주소 : /myPage/file/a.jpg
		
		
		
		return "/myPage/file/" + uploadFile.getOriginalFilename();
	}
	
	
	
		
	}
	
	
	
