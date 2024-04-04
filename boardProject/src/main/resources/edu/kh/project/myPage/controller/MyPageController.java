package edu.kh.project.myPage.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.RequiredArgsConstructor;

@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("myPage")
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService service;
	
	
	/**
	 * 내 정보 조회 + 수정화면
	 * 
	 * @param loginMember : 세션에 존재하는 loginMember을 얻어와서 매개변수에 대입
	 * @param model : 데이터 전달용 객체(기본 request scope)
	 * @return myPage/myPage-info
	 */
	@GetMapping("info") // /myPage/info Get방식 요청이 들어올 경우 수행
	public String info(@SessionAttribute("loginMember") Member loginMember, Model model) {
		
		
		String memberAddress = loginMember.getMemberAddress();
		/* 서버(Controller)에서 로그인한 회원 주소(address)를 우편번호/도로명 주소/상세 주소 3개로 쪼개기 위해
		 * 주소를 얻어옴
		 */
		
		
		// 주소는 not null이 아니기에 주소를 적은 사람들만 해당하도록 함
		if(memberAddress != null) {
			String[] arr = memberAddress.split("\\^\\^\\^");
			
			// 구분자 ^^^ 를 기준으로 memberAddress 값을 쪼개어서 String[]으로 반환하게함
			// ^는 다른 기능을 수행하는 구별자이므로 \\를 사이사이에 붙임
			model.addAttribute("postcode",arr[0]);
			model.addAttribute("address",arr[1]);
			model.addAttribute("detailAddress",arr[2]);
		}
		
		
		
		// myPage 폴더의 myPage-info.html로 forward
		return "myPage/myPage-info";
	}
	
	
	
	@GetMapping("profile")
	public String profile() {
		
		return "myPage/myPage-profile";
	}
	
	
	
	
	@GetMapping("changePw")
	public String changePw() {
		
		return "myPage/myPage-changePw";
	}
	
	
	
	@GetMapping("secession")
	public String secession() {
		
		return "myPage/myPage-secession";
	}
	
	
	
	/**
	 * info 페이지에서 회원 정보 수정
	 * @param inputMember : 새롭게 수정할 입력된 회원 정보
	 * @param loginMember : 로그인한 회원의 정보(그 중에서 회원 번호를 사용할 예정)
	 * @param memberAddress : 따로받아온 회원 주소 String[]
	 * @param ra : Redirect시 request scope로 데이터를 전달하는 역할
	 * @return
	 */
	@PostMapping("info")
	public String updateInfo( /* @ModelAttribute */ Member inputMember,
			@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("memberAddress") String[] memberAddress,
			RedirectAttributes ra) {
		// 수정할 정보를 받은 inputMember과 더불어서 Session에서 로그인한 회원의 회원번호(primary key)의 정보를 수정하도록 함
		
		
		// inputMember에 로그인한 회원 번호를 추가함
		int memberNo = loginMember.getMemberNo();
		
		inputMember.setMemberNo(memberNo);
		
		
		// 회원 정보 수정 서비스를 호출하기
		int result = service.updateInfo(inputMember, memberAddress);
		
		String message = null;
		
		
		if(result > 0) {
			message = "회원 정보 수정 성공";
			
			// loginMember은 session에 저장된 로그인 회원 정보가 저장된 객체를 참조하고 있다. 
			// loginMember을 수정하면 session에 저장된 로그인상태 회원 정보가 수정됨 == session 데이터가 수정됨
			
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());
			
			
		} else {
			message = "회원 정보 수정 실패";
			
		}
			ra.addFlashAttribute("message",message);
		
		
		
		// 수정 성공/실패 여부 상관없이 처리하고 나면 info 페이지로 redirect
		return "redirect:info";
	}
	
	
	
	
	@ResponseBody
	@GetMapping("selectPw")
	public String selectPw() {
		return "myPage/myPage-changePw";
	}
	
	
	@PostMapping("changePw")
	public String changePw(Member inputMember, @SessionAttribute("loginMember") Member loginMember,
			@RequestParam Map<String,Object> paramMap,
			RedirectAttributes ra)
			  {
		
		int memberNo = loginMember.getMemberNo();
		
		
		// 현재 비밀번호 +새로운 비밀번호+회원번호를 서비스로 전달
		int result = service.changePw(paramMap,memberNo);
		
		String path = null;
		String message = null;
		
		if (result > 0) {
			path = "/myPage/info";
			message = "비밀번호가 변경되었습니다.";
			
		} else {
			message = "실패";
			path = "/myPage/changePw";
		}
		ra.addFlashAttribute("message",message);
		
		return "redirect:" + path;
		
		
		
	}
	
	
	// 비밀 번호를 입력받고, 비밀번호가 일치 + 약관 일치버튼을 클릭한 경우 해당 회원을 탈퇴(탈퇴 여부를 'Y'로 전환)
	
	/*
	 * [컨트롤러]
	 * - 파라미터 : 입력된 비밀번호
	 * - Session : 로그인한 회원 정보 -> 회원 번호만 얻어오기 (누가 탈퇴하는지 지정하는 용도)
	 * 
	 * [서비스]
	 * - 현재 로그인한 회원의 암호화된 비밀번호를 DB에서 조회를 함
	 * - 입력된 비밀번호와 비교해서 일치할 경우, MEMBER_DEL_FL 값을 'Y'로 변경하는 MAPPER 호출 -> 수행 결과를 반환
	 * - 불일치할 경우, return 0;
	 * 
	 * [컨트롤러]
	 * - 결과에 따라서 탈퇴 성공하면 '회원탈퇴되었습니다.'하고 메인페이지로 redirect
	 * - 회원 로그아웃시킴
	 * - 실패하면(비밀번호가 불일치) '탈퇴 실패'하고 탈퇴 페이지로 redirect
	 */
	
	
	
	/**
	 * 회원 탈퇴
	 * @param loginMember : memberNo를 가져오기 위한 용도
	 * @param memberPw : 입력받은 비밀번호
	 * @param ra : 메시지 전달용
	 * @param status : 세션 완료시킴(없앰)
	 * @return
	 */
	
	// @SessionAttribute : Model에 세팅된 값 중 key가 일치하는 값을 request -> session으로 변경
	// SessionStatus : @SessionAttributes를 이용해서 올라간 데이터의 상태를 관리하는 객체
	//               -> 해당 컨트롤러에 @SessionAttributes({"key1","key2"})가 작성되어 있는 경우 () 내 key1, key2의 상태를 관리
	
	
	@PostMapping("secession")
	public String secession(@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("memberPw") String memberPw, RedirectAttributes ra, 
			SessionStatus status) {
		
		int memberNo = loginMember.getMemberNo();
		

		
		int result = service.secession(memberPw,loginMember.getMemberNo());
		
		String path = null;
		String message = null;
		
		if(result > 0) {
			
			path = "/";
			
			message = "회원탈퇴 성공";
			
			status.setComplete();
			
		} else {
			
			path = "/myPage/secession";
			
			message = "탈퇴 실패";
			
		}
		
		ra.addFlashAttribute("message",message);
		return "redirect:" + path;
	}

	
	// 파일 업로드 테스트
	
	@GetMapping("fileTest")
	public String fileTest() {
		
		
		return "myPage/myPage-fileTest";
		
	/*
	 * HTML에서 서버로 제출하는 데이터(파라미터)는 String 형태.
	 */
	}
	
	
	
	/*
	 * Spring에서 파일 업로드를 처리하는 방법
	 * 
	 * - enctype = "multipart/form-data"로 클라이언트 요청을 받으면
	 * 	(문자, 숫자, 파일 등이 섞여있는 요청)
	 * 
	 * 이를 MultipartResolver를 이용해서 섞여있는 파라미터를 분리
	 * 
	 * 문자열, 숫자 -> String
	 * 파일 -> MultipartFile
	 */
	
	
	// 파일 업로드 테스트
	@PostMapping("file/test1")
	public String fileUpload1(
			@RequestParam("uploadFile") MultipartFile uploadFile, RedirectAttributes ra) throws IllegalStateException, IOException {
		
		
		// 실제로 업로드한 파일과 설정내용이 담겨있음
		String path = service.fileUpload1(uploadFile);
		
		if(path != null) {
			// 파일이 저장되어 웹에서 접근할 수 있는 경로가 반환되었을 경우
			ra.addFlashAttribute("path",path);
		}
		
		return "redirect:/myPage/fileTest";
	}
	
	
	
	
}
