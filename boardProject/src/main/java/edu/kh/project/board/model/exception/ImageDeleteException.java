package edu.kh.project.board.model.exception;



/**
 * 게시글 삽입 중 문제 발생 시 사용할 사용자 정의 예외
 */
public class ImageDeleteException extends RuntimeException{
	
	
	public ImageDeleteException() {
		
		super("이미지 제거 중 예외 발생");
	}
	
	
	
	public ImageDeleteException(String message) {
		
		super(message);
	}
	
	

}
