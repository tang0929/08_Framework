package edu.kh.project.member.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper

public interface MemberMapper {
	
	/**
	 * 로그인 SQL 실행
	 * @param memberEmail
	 * @return loginMember
	 */
	
	public Member login(String memberEmail);

	
	/**
	 * 회원 가입 SQL실행
	 * @param inputMember
	 * @return
	 */
	public int signup(Member inputMember);

}
