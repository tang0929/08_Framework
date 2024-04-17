package edu.kh.project.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.exception.BoardInsertException;
import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.exception.ImageDeleteException;
import edu.kh.project.board.model.exception.ImageUpdateException;
import edu.kh.project.board.model.mapper.EditBoardMapper;
import edu.kh.project.common.util.Utility;
import lombok.RequiredArgsConstructor;
import oracle.net.aso.l;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class) // 예외를 unchecked checked 상관 없이 모조리 처리함
@PropertySource("classpath:/config.properties")
public class EditBoardServiceImpl implements EditBoardService{

	
	private final EditBoardMapper mapper;
	
	// config.properties 값을 얻어와 필드에 저장
	@Value("${my.board.web-path}")
	private String webPath;
	
	@Value("${my.board.folder-path}")
	private String folderPath;
	
	
	// 게시글 작성
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) throws IllegalStateException, IOException {
		
		
		// 1. 게시글 부분(inputBoard) 먼저 BOARD 테이블에 INSERT하기
		// -> INSERT 결과로ㅈ 작성된 ㄴ게시글 번호(생성된 시퀀스 번호) 반환
		
		int result = mapper.boardInsert(inputBoard);
		
		
		
		// mapper.xml에서 selectKey 태그를 이용해 생성된  boardNo가 inputNo에 저장된 상태
		
		
		// 삽입 실패시
		if(result == 0) return 0;
		
		
		//삽입ㄷ괸 게시글의 번호를 변수로 저장
		int boardNo = inputBoard.getBoardNo();
		
		
		
		//    "BOARD_IMG" 테이블에 삽입하는 코드 작성
		
		
		
		// 실제 업로드된 이미지의 정보를 모아둘 List 생성
		List<BoardImg> uploadList = new ArrayList<>();
		
		
		
		// images 리스트에서 하나씩 꺼내어 선택된 파일이 있는지 검사
		for(int i = 0; i < images.size() ; i++) {
			
			
			// 선택된 파일이 존재하는 경우(= 파일이 비어있지 않을 때)
			if(!images.get(i).isEmpty()) {
				
				// IMG_PATH == webPath
				// BOARD_NO == boardNo
				// IMG_ORDER == i (인덱스 == 순서)
				
				// 원본명
				String originalName = images.get(i).getOriginalFilename();
				
				
				// 변경명
				String rename = Utility.fileRename(originalName);
				
				
				// 모든 값을 저장한 DTO를 생성(Builder를 import 후 사용)
				BoardImg img = BoardImg.builder()
						.imgOriginalName(originalName)
						.imgRename(rename)
						.imgPath(webPath)
						.boardNo(boardNo)
						.imgOrder(i)
						.uploadFile(images.get(i))
						.build();
				
				uploadList.add(img);
			}
		}

		
		// 실제 선택한 파일이 없을 경우
		if(uploadList.isEmpty()) {
			
			return boardNo;
			
		}
		
		// 선택한 파일이 존재하는 경우
		// -> "BOARD_IMG" 테이블에 INSERT + 서버에 파일 저장
		
		
		/*
		 * 여러 행 삽입 방법
		 * 
		 * 1. 1행 삽입하는 SQL을 for문을 이용해서 여러 번 호출
		 * 
		 * 2. 여러 행을 삽입하는 SQL을 1회 호출 (사용할 방식)
		 */
		
		
		// result == 삽입된 행의 개수 == uploadList.size()
		result = mapper.insertUploadList(uploadList);
		
		
		// 다중 INSERT 성공 확인(uploadList에 저장된 값이랑 일치하는지 확인)
		if(result == uploadList.size()) {
			
			// 서버에 파일 저장
			
			for(BoardImg img : uploadList) {
				
				img.getUploadFile()
				.transferTo(new File(folderPath + img.getImgRename()));
				
			}
			
		} else {
			
			/*
			 * 부분적으로만 삽입 실패 한 경우, 전체 서비스가 실패했다고 판단을 하고,
			 * 삽입 성공한 내용들도 rollback을 진행해야 함
			 * rollback은 @Transactional을 통해 RuntimeException을 발생시키는 방법으로 함
			 */
			throw new BoardInsertException("이미지 삽입 중 오류 발생");
		}
		
		return boardNo;
	}
	
	
	
	
	/**
	 * 게시글 삭제
	 */
	@Override
	public int boardDelete(int boardNo, int boardCode) {
		
		Map<String, Integer> map = new HashMap<>();
		
		map.put("boardNo", boardNo);
		map.put("boardCode", boardCode);
		
		return mapper.boardDelete(map);
	}
	
	
	
	
	/**
	 * 게시글 수정
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@Override
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrder) throws IllegalStateException, IOException {
		
		
		// 1. 게시글 부분을 수정(제목/내용)
		int result = mapper.boardUpdate(inputBoard);
		
		
		
		// 2. 기존 이미지가 있었다가 삭제된 이미지(deleteOrder)가 있는 경우
		if(deleteOrder != null && !deleteOrder.equals("")) {
			
			Map<String, Object> map = new HashMap<>();
			map.put("deleteOrder", deleteOrder);
			map.put("boardNo", inputBoard.getBoardNo());
			
			result = mapper.deleteImage(map);
			
			
			// 부분 실패 포함 삭제 실패일 경우
			if(result == 0) {
				
				throw new ImageDeleteException();
			}
			
		}
		
		
		
		
		// 3. 선택한 파일이 존재할 경우 해당 파일 정보만 모아두는 List를 생성(INSERT 구문의 이미지 추가부분 재활용한 코드)
		
		List<BoardImg> uploadList = new ArrayList<>();
		
		
		
		// images 리스트에서 하나씩 꺼내어 선택된 파일이 있는지 검사
		for(int i = 0; i < images.size() ; i++) {
			
			
			// 선택된 파일이 존재하는 경우(= 파일이 비어있지 않을 때)
			if(!images.get(i).isEmpty()) {
				
				// IMG_PATH == webPath
				// BOARD_NO == boardNo
				// IMG_ORDER == i (인덱스 == 순서)
				
				// 원본명
				String originalName = images.get(i).getOriginalFilename();
				
				
				// 변경명
				String rename = Utility.fileRename(originalName);
				
				
				// 모든 값을 저장한 DTO를 생성(Builder를 import 후 사용)
				BoardImg img = BoardImg.builder()
						.imgOriginalName(originalName)
						.imgRename(rename)
						.imgPath(webPath)
						.boardNo(inputBoard.getBoardNo())
						.imgOrder(i)
						.uploadFile(images.get(i))
						.build();
				
				uploadList.add(img);
				
				
				// 4. 업로드하려는 이미지 정보(img)를 이용해 수정 또는 삽입을 수행
				
				// 1) 기존에 이미지 있었고 새 이미지로 변경 후 수정
				result = mapper.updateImage(img);
				
				if(result == 0) {
					// 수정 실패 == 기존 해당 순서(imgOrder)에 이미지가 없었다 
					// -> 삽입을 수행해야함
					
				// 2) 기존에 이미지가 없어서 새 이미지를 추가함
				result = mapper.insertImage(img);
				
			}
					
				
		}
			
			// 수정 또는 삭제가 실패한 경우
			if(result == 0) {
				
				throw new ImageUpdateException(); // 예외 발생시켜서 롤백
			}
			
			
			
	}

		
		
		
		// 실제 선택한 파일이 없을 경우
		if(uploadList.isEmpty()) {
			
			return result;
			
		}
		
		// 수정/삭제된 이미지 파일을 서버에 저장(INSERT 구문의 이미지 파일 저장 부분 재활용한 코드)
		
	
			
			// 서버에 파일 저장
			
			for(BoardImg img : uploadList) {
				
				img.getUploadFile()
				.transferTo(new File(folderPath + img.getImgRename()));
				
			}
			
		
		
		
		return result;
}

	
}