package edu.kh.project.myPage.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.RequiredArgsConstructor;

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
		
		
		
		int result = service.changePw(memberNo,paramMap);
		
		String path = null;
		String message = null;
		
		if (result > 0) {
			message = "비밀번호가 변경되었습니다.";
			path = "/myPage/info";
		} else {
			message = "실패";
			return "/myPage/changePw";
		}
		ra.addFlashAttribute("message",message);
		
		return "redirect:" + path;
		
		
		
		
		
	
		
	}

	
	
	
	
}
