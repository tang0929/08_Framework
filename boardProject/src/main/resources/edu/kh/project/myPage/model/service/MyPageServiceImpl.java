package edu.kh.project.myPage.model.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.Utility;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)// 모든 예외 발생시 롤백
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
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
	
	
	@Value("${my.profile.web-path}")
	private String profileWebPath; //  /myPage/profile/
	
	@Value("${my.profile.folder-path}")
	private String profileFolderPath; // C:/uploadFiles/profile/
	
	
	
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
	
	
	
	
	/**
	 * 파일 업로드 2 + DB
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@Override
	public int fileUpload2(MultipartFile uploadFile, int memberNo) throws IllegalStateException, IOException {
		
		// 업로드된 파일이 없다면 더 이상 실행하지 않음
		if(uploadFile.isEmpty()) {
			return 0;
		}
		
		
		/*
		 * DB에는 파일 자체를 저장이 가능은 하지만, 그리 추천하는 방법이 아님. 부하를 줄이기 위해서
		 * 
		 * 1) DB에는 서버에 저장할 파일 경로를 저장
		 * 2) DB에 삽입/수정 성공 후 서버에 파일을 저장함
		 * 3) 파일 저장을 실패하면 예외 발생(@Transactional를 통해 rollback)
		 */
		
		
		// 1) 서버에 저장할 파일 경로 만들기
		// folderpath : 파일이 저장될 서버 폴더 경로
		String folderPath = "C:\\uploadFiles\\test\\";
		
		
		// webPath : 클라이언트가 파일이 저장된 폴더에 접근할 수 있는 주소
		String webPath = "/myPage/file/";
		
		
		
		// 2) DB에 저장할 데이터를 DTO으로 묶어서 Insert 호출하기
		// webPath, memberNo, 원본 파일명, 변경된 파일명을 데이터로 함
		
		String fileRename = Utility.fileRename(uploadFile.getOriginalFilename());
		
		
		
		// Builder 패턴을 이용해서 UploadFile 객체 생성
		// 반복되는 참조 변수명, set 구문 생략 가능. method chaining을 이용해서 한 줄로 작성 가능
		UploadFile uf = UploadFile.builder()
				.memberNo(memberNo)
				.filePath(webPath)
				.fileOriginalName(uploadFile.getOriginalFilename())
				.fileRename(fileRename)
				.build();
		
		
		
		
		
		int result = mapper.insertUploadFile(uf);
		
		
		
		
		// 3. 삽입(INSERT) 성공시 파일을 지정된 서버 폴더에 저장
		
		// 삽입 실패시
		if(result == 0) {
			
			return 0;
			
		} // 삽입 성공
		// folderPath // 변경된 파일명으로 파일을 서버에 저장
		uploadFile.transferTo(new File(folderPath + fileRename));
		// CheckedException 발생 -> 예외처리해야함. @Transactional은 UncheckedEception만 처리하므로
		// rollbackFor 속성을 이용해서 rollbackFor 속성을 이용해 롤백할 예외 범위 수정
		
		return result; // 1
		
	}
	
	
	
	
	@Override
	public List<UploadFile> fileList() {
		
		
		
		return mapper.fileList();
	}
	
	
	
	
	@Override
	public int fileUpload3(List<MultipartFile> uploadFile2List, List<MultipartFile> uploadFile3List, int memberNo)
			throws IllegalStateException, IOException {
		
		
		int result1 = 0;
		// 1. uploadFile2List 처리
		
		// 업로드된 파일이 없을 경우를 제외하고 업로드하기 
		for(MultipartFile file : uploadFile2List) {
			if(file.isEmpty()) { // 파일 없으면 다음 위치에 파일있는지 확인하려 넘어감
				continue;
			}
			
			// fileUpload2() 메서드 호출
			result1 += fileUpload2(file,memberNo);
			
		}
		
	
	
	
	
	
	// 2. uploadFile3List 처리

	
	int result2 = 0;
	
			// 업로드된 파일이 없을 경우를 제외하고 업로드하기 
			for(MultipartFile file : uploadFile3List) {
				if(file.isEmpty()) { // 파일 없으면 다음 위치에 파일있는지 확인하려 넘어감
					continue;
				}
				
				// fileUpload2() 메서드 호출
				result2 += fileUpload2(file,memberNo);
				
			}
	
	
			return result1 + result2;
	}


	
	// profile 이미지 변경
	@Override
	public int profile(MultipartFile profileImg, Member loginMember) throws IllegalStateException, IOException {
		
		// 수정할 경로
		String updatePath = null;
		
		// 변경이름 저장
		String rename = null;
	
		
		// 업로드한 이미지가 있을경우
		if(!profileImg.isEmpty()) {
			
			// updatePath 조합
		
			
			 rename = Utility.fileRename(profileImg.getOriginalFilename());
			
			// /myPage/profile/변경된파일명.jpg
			updatePath = profileWebPath + rename;
			
		}
		
		
		// 수정된 프로필 이미지 경로, 회원 번호를 저장할 DTO 객체
		Member mem = Member.builder()
				.memberNo(loginMember.getMemberNo())
				.profileImg(updatePath)
				.build();
		
		
		
		// UPDATE 수행
		int result = mapper.profile(mem);
		
		
		if(result > 0) {
			// 수정 성공하면 
			
			if(!profileImg.isEmpty()) {
			// 프로필 이미지를 없애는 쪽으로 수정한 경우(null로 수정한 경우)를 제외 -> 업로드한 이미지가 실제로 있을 경우
			// 업로드한 파일을 서버의 지정된 폴더에 저장
			profileImg.transferTo(new File(profileFolderPath + rename));
			}
			
			// 세션 회원 정보에서 프로필 이미지 경로를 업데이트한 경로로 변경
			loginMember.setProfileImg(updatePath);
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public int fileUpload3(List<MultipartFile> uploadFile2List, List<MultipartFile> uploadFile3List)
			throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	







	}









	
	

	
