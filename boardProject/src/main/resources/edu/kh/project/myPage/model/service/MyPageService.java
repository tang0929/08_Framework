package edu.kh.project.myPage.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

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



	/**
	 * 파일 업로드2 + DB
	 * @param uploadFile
	 * @param memberNo
	 * @return
	 */
	int fileUpload2(MultipartFile uploadFile, int memberNo) throws IllegalStateException, IOException;



	/**
	 * 업로드 파일 목록 조회
	 * @return
	 */
	List<UploadFile> fileList();


	/**
	 * 파일업로드3
	 * @param uploadFile2List
	 * @param uploadFile3List
	 * @param memberNo 
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	int fileUpload3(List<MultipartFile> uploadFile2List, List<MultipartFile> uploadFile3List, int memberNo) 
			throws IllegalStateException, IOException;



	int fileUpload3(List<MultipartFile> uploadFile2List, List<MultipartFile> uploadFile3List)
			throws IllegalStateException, IOException;



	int profile(MultipartFile profileImg, Member loginMember) throws IllegalStateException, IOException;








}
