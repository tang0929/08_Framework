package edu.kh.project.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

// 프로그램에 전체적으로 사용될 유용한 기능들 모음
public class Utility {

	
	public static int seqNum = 1; // 1~99999 반복
	/**
	 * 
	 * @param originalFileName
	 * @return
	 */
	public static String fileRename(String originalFileName) {
		
		// static을 붙인 이유 : 프로그램이 시작하자마자 static 영역의 이름을 다 실행함. 
		
		// 변경될 파일명은 최대한 중복이 안되는 형식으로 작성. ex)현재 날짜/시간
		// 202404051010_00001.jpg 형식으로 만들 예정(현재날짜시간분초/00001)
		
		
		// SimpleDateFormat : 시간을 원하는 형태의 문자열로 간단히 변경. 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		
		// new java.util.Date() : 현재 시간을 저장하는 java 객체
		String date = sdf.format(new java.util.Date());
		
		
		String number = String.format("%05d", seqNum);
		
		
		// 1증가
		seqNum++;
		
		
		// seqNum이 99999를 초과하면 1로 설정
		if(seqNum == 100000) seqNum = 1;
		
		
		
		// 확장자
		// 문자열.subString(index) : 문자의 index부터 끝까지 잘라낸 결과를 반환함
		
		// 문자열.lastIndexOf(".") : 문자열에서 마지막 "."의 인덱스를 반환
		
		
		String ext = originalFileName.substring(originalFileName.lastIndexOf("."));
		// .을 포함한 확장자가 출력됨. (.jpg)
		
		
		return date + "_" + number + ext;
	}
}
