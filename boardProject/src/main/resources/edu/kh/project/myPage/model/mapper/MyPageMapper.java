package edu.kh.project.myPage.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

@Mapper
public interface MyPageMapper {

	
	/**
	 * 회원 정보 수정함
	 * @param inputMember
	 * @return
	 */
	int updateInfo(Member inputMember);

	int changePw(Member inputMember, String currentPw, String newPw);

	String selectPw(int memberNo);

	int changePw(Map<String, Object> map);

	int secession(int memberNo);
	
	
	/**
	 * 파일 정보를 DB에 삽입하는 문구
	 * @param uf
	 * @return
	 */
	int insertUploadFile(UploadFile uf);

	
	
	/** 업로드한 파일 목록 조회
	 * 
	 * @return
	 */
	List<UploadFile> fileList();

	
	
	int profile(Member mem);

}
