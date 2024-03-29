package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/* @SessionAttributes( {"key", "key", ...} )
 * 
 * - Model에 추가된 속성(Attribute) 중
 *   key 값이 일치하는 속성을 session scope로 변경
 */

@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("member") 
@Slf4j
public class MemberController {

	@Autowired // 의존성 주입(DI)
	private MemberService service;
	
	
	/* [로그인] 
	 *  - 특정 사이트에 아이디/비밀번호 등을 입력해서
	 *    해당 정보가 있으면 조회/서비스 이용
	 *    
	 *  - 로그인한 정보를 session에 기록하여
	 *    로그아웃 또는 브라우저 종료 시 까지
	 *    해당 정보를 계속 이용할 수 있게 함
	 * */
	
	
	// 커맨드 객체 
	// - 요청 시 전달 받은 파라미터를
	//   같은 이름의 필드에 세팅한 객체
	
	/** 로그인
	 * @param inputMember : 커맨드 객체 (@ModelAttribute 생략)
	 * 					   (memberEmail, memberPw 세팅된 상태)
	 * 
	 * @param ra : 리다이렉트 시 request scope로 데이터를 전달하는 객체
	 * 
	 * @param saveId : 아이디 저장 체킁 ㅕ부
	 * 
	 * @param resp : 쿠키 생성, 추가를 위해 얻어온 객체
	 * 
	 * @param model: 데이터 전달용 객체(request scope)
	 * 
	 * @return "redirect:/"
	 */
	@PostMapping("login")
	public String login(
		Member inputMember,
		RedirectAttributes ra,
		// saveId는 필수적으로 요구되는 사항이 아님
		Model model, @RequestParam(value = "saveId", required = false) String saveId,
		HttpServletResponse resp) {
		
		// 로그인 서비스 호출
		Member loginMember = service.login(inputMember);
		
	
		// 체크박스에 value가 없을 때 
		
		// 체크가 된 경우 : on (null이 아님)
		 
		// 체크가 안된 경우 : null
		
		
		// 로그인 실패 시 
		if(loginMember == null) {
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다");
		}
		
		// 로그인 성공 시
		if(loginMember != null) {
			// Session scope에 loginMember 추가
			
			model.addAttribute("loginMember", loginMember);
			// 1 단계 : request scope에 세팅됨
			
			// 2 단계 : 클래스 위에 @SessionAttributes() 어노테이션 때문에
			//			session scope로 이동됨
			
			
			
			// 아이디 저장(Cookie)
			// 쿠키 객체 생성(K:V)
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			
			
			// 클라이언트가 어떤 요청을 할 때 쿠키가 첨부될지 지정
			// ex) "/" : IP , 도메인 또는 localhost + 그 하위 주소
			cookie.setPath("/");
			
			
			// 만료 기간 지정
			if(saveId != null ) { // 아이디 저장 체크시
				cookie.setMaxAge(30*24*60*60); // 초 단위로 지정(30일)
			} else {
				cookie.setMaxAge(0);
			}
			
			// d응답 객체에 쿠키 추가 -> 클라이언트 전달
			resp.addCookie(cookie);
		}
		
		
		return "redirect:/"; // 메인페이지 재요청
	
	
	}
	
	
	/**
	 * 로그 아웃 : Session에 저장된 로그인된 정보를 제거(만료시킴, 무효화시킴, 완료시킴)
	 * @param SessionStatus : @SessionAttributes로 등록된 세션을 완료(제거)시키는 역할의 객체
	 * 						서버에서 기존 세션 객체가 사라짐과 동시에 새로운 세션 객체가 생성되어 클라이언트와 연결
	 * 			
	 * @return
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		
		
		status.setComplete(); // 세션을 완료시킴(제거시킴)
		
		
		return "redirect:/";  // 세션 완료 후 메인페이지로
	}
	
	
	
	
	/**
	 * 로그인 전용 페이지로 이동
	 * @return
	 */
	@GetMapping("login")
	public String loginPage() {
		return "member/login";
	}
	
	
	
	/**
	 * 회원가입 전용 페이지
	 * @return
	 */
	@GetMapping("signup")
	public String signupPage()	{
		return "member/signup";
	}
	
	
	
	/**
	 * 회원가입 페이지에서 받는 상세 내용들
	 * @param inputMember -> memberEmail, memberPw, memberNickname, memberTel가 ModelAttribute를 통해 담겨짐
	 * @param memberAddress -> 입력한 주소가 3칸이므로(우편번호/도로명주소/상세주소) input 3개의 값을 배열로 전달함
	 * @param ra -> redirect시 request scope로 데이터를 전달하는 객체
	 * @return
	 */
	@PostMapping("signup")
	public String signup(/* @ModelAttribute */ Member inputMember,
							@RequestParam("memberAddress") String[] memberAddress,
							RedirectAttributes ra) {
		
		
		// 회원가입 서비스를 호출하기 : INSERT를 통해 변경된 행의 수 INT를 받아옴
		int result = service.signup(inputMember,memberAddress);
		
		
		String path = null;
		String message = null;
		if(result > 0) {
			message = "★★★★"+inputMember.getMemberNickname()+"님의 회원 가입을 환영합니다★★★★";
			path = "/"; // 경로를 메인페이지로 
		} else {
			message = "회원가입 실패";
			path = "signup"; // 회원가입 화면으로 다시 돌아가게함
		}
			ra.addFlashAttribute("message",message);
		
		return "redirect:" + path;
	}
	
	
	
	/**
	 * 이메일 중복 검사
	 * 
	 * @param memberEmail
	 * @return : 중복이면 1, 아니면 0으로
	 */
	@ResponseBody // 응답 본문(요청한 fetch())로 돌려보냄
	@GetMapping("checkEmail")
	public int checkEmail(@RequestParam("memberEmail") String memberEmail) {
		
		return service.checkEmail(memberEmail);
	}
	
	
	/**
	 * 닉네임 중복 검사
	 */
	@ResponseBody 
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("memberNickname") String memberNickname) {
		
		return service.checkNickname(memberNickname);
	}
	
}







//***********************************************************************

		
			
			/* Cookie란?
			 * - 클라이언트 측(브라우저)에서 관리하는 데이터(파일 형식)
			 * 
			 * - Cookie는 만료기간, 데이터(key=value), 사용하는 사이트(주소)가 기록되어있음
			 * 
			 * - 클라이언트가 Cookie에 긹된 사이트로 요청으로 보낼때, 요청에 쿠키가 담겨져서 서버로 넘어감
			 * 
			 * - Cookie의 생성, 수정, 삭제는 Server가 관리하고 저장은 클라이언트가 함
			 * 
			 * - Cookie는 HttpServletResponse를 이용해서 생성하고 클라이언트에게 전달(응답)
			 */
			
			
// ***********************************************************************
			



