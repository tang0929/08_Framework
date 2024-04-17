package edu.kh.project.board.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;

@Service
public interface EditBoardService {

	int boardInsert(Board inputBoard, List<MultipartFile> images) throws IllegalStateException, IOException;



	int boardDelete(int boardNo, int boardCode);



	/**
	 * 게시글 수정
	 * @param inputBoard
	 * @param images
	 * @param deleteOrder
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrder) throws IllegalStateException, IOException;

}
