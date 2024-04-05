package edu.kh.project.myPage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder // Builder 패턴을 이용해서 객체 생성 및 초기화를 쉽게 진행 -> 기본 생성자가 생성이 안됨 
			// -> MyBatis 조회 결과를 담을 때 필요한 객체 생성을 실패함
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
	
	private int fileNo;
	private String filePath;
	private String fileOriginalName;
	private String fileRename;
	private String fileUploadDate;
	private int memberNo;
	
	private String memberNickname;
	

}
