package edu.kh.project.myPage.model.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;

public interface MyPageService {

	/**
	 * 회원 정보 수정
	 * @param inputMember
	 * @param memberAddress
	 * @return
	 */
	int updateInfo(Member inputMember, String[] memberAddress);



	/**
	 * 현재 비밀번호 변경
	 */


	int changePw(Map<String, Object> paramMap, int memberNo);



	/**
	 * 회원 탈퇴
	 * @param memberPw
	 * @param memberNo
	 * @return result
	 */
	int secession(String memberPw, int memberNo);



	/**
	 * 파일 업로드
	 * @param uploadFile
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	String fileUpload1(MultipartFile uploadFile) throws IllegalStateException, IOException;








}
