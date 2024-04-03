package edu.kh.project.member.model.service;

import java.util.List;

import edu.kh.project.member.model.dto.Member;

public interface MemberService {

	
	/**
	 * 로그인 서비스
	 * @param inputMember
	 * @return loginMember
	 */
	Member login(Member inputMember);

	
	
	/**
	 * 회원가입시 받아올 내용 서비스
	 * @param inputMember
	 * @param memberAddress
	 * @return
	 */
	int signup(Member inputMember, String[] memberAddress);



	/**
	 * 이메일 중복 검사를 위한 0 혹은 1 반환
	 * @param memberEmail
	 * @return
	 */
	int checkEmail(String memberEmail);



	/**
	 * 닉네임 중복 검사 
	 * @param memberNickname
	 * @return
	 */
	int checkNickname(String memberNickname);



	/**
	 * 바른 로그인
	 * @param memberEmail
	 * @return loginMember
	 */
	Member quickLogin(String memberEmail);



	List<Member> selectMemberList();









}
