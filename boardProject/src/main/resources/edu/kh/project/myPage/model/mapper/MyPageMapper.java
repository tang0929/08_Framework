package edu.kh.project.myPage.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

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

}
