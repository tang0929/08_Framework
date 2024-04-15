package edu.kh.project.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.kh.project.board.model.dto.Board;

@Mapper
public interface BoardMapper {

	List<Map<String, Object>> selectBoardTypeList();

	/**
	 * 게시글 수 조회
	 * @param boardCode
	 * @return
	 */
	int getListCount(int boardCode);

	
	
	/**
	 * 특정 게시판의 지정된 페이지 목록을 조회
	 * @param boardCode
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectBoardList(int boardCode, RowBounds rowBounds);

	
	/**
	 * 게시판 상세 조회
	 * @param map
	 * @return
	 */
	Board selectOne(Map<String, Integer> map);

	
	/**
	 * 좋아요 해제
	 * @param map
	 * @return
	 */
	int deleteBoardLike(Map<String, Integer> map);

	
	
	/**
	 * 좋아요 체크
	 * @param map
	 * @return
	 */
	int insertBoardLike(Map<String, Integer> map);

	
	
	/**
	 * 좋아요 개수 조회
	 * @param temp 
	 * @return count
	 */
	int selectLikeCount(int temp);

	
	
	/**
	 * 조회수 1 증가
	 * @param boardNo
	 * @return
	 */
	int updateReadCount(int boardNo);

	
	
	/**
	 * 조회수 조회
	 * @param boardNo
	 * @return
	 */
	int selectReadCount(int boardNo);

}
