package edu.kh.project.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.Pagination;
import edu.kh.project.board.model.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor

public class BoardServiceImpl implements BoardService{
	
	
	// mapper 의존성 주입을 하기 위한 RequiredArgsConstructor을 위한 final 선언
	private final BoardMapper mapper;
	
	
	
	
	// 게시판 종류 조회
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
		
		return mapper.selectBoardTypeList();
	}
	
	
	
	
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {
		
		
		
		
		// 1. 지정된 게시판애서 삭제되지 않은 게시글 수를 조회
		int listCount = mapper.getListCount(boardCode);
		
		
		// 2. 1번의 결과 + cp 이용해서 Pagination 객체를 생성
		Pagination pagination = new Pagination(cp, listCount);
		
		
		// pagination : 게시글 목록 구성에 필요한 값을 저장하는 개체
		
		
		// 3. 특정 게시판의 지정된 페이지 목록 조회
		/**
		 * ROWBOUNDS 객체(Mybatis 제공 객체)
		 * - 지정된 크기(offset)만큼 건너뛰고 제한된 크기(limit)만큼의 행을 조회하는 객체 
		 * - 페이징 처리가 굉장히 간단해짐
		 */
		
		int limit = pagination.getLimit();
		int offset = (cp - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		
		/*
		 * Mapper 메서드 호출 시 
		 * 첫번째 매개변수 -> SQL에 전달할 파라미터
		 * 두번째 매개변수 -> RowBounds 객체 전달 
		 */
		List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
		
		
		// 4. 목록 조회한 결과 + pagination객체르 Map으로 묶음
		Map<String, Object> map = new HashMap<>();
		
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		//  5. 결과 반환
		
		return map;
	}

	
	
	
	
	// 게시글 상세 조회
	@Override
	public Board selectOne(Map<String, Integer> map) {
		
		// [여러 SQL을 실행하는 방법]
		// 1. 하나의 Service 메서드에서
		//    여러 Mapper 메서드를 호출하는 방법
		
		/*
		 *  2. 수행하려는 SQL이 
		 * 1) 모두 SELECT 이면서
		 * 2) 먼저 조회된 결과 중 일부를 이용해 나중에 수행되는 SQL의 조건으로 삼을 때
		 *  --> Mybatis의 <resultMap>, <collection> 태그를 이용해 Mapper 메서드 1회 호출로 여러 SELECT 한 번에 수행 가능 
		 *  2번을 통한 방법 사용*/
		
		return mapper.selectOne(map);
	}
	
	
	
	/**
	 * 게시글 좋아요 체크/해제
	 */
	@Override
	public int boardLike(Map<String, Integer> map) {
	
		int result = 0;
		
		
		// 1. 좋아요가 체크된 상태일 경우(likeCheck == 1) 누르면 취소로 변경(BOARD_LIKE 테이블에 DELETE)
		 
		if(map.get("likeCheck") == 1) {
			
			// map.get("likeCheck")는 Integer 타입인데, 1은 int 타입인데도 불구하고 AutoUnboxing이 수행됨
			
			result = mapper.deleteBoardLike(map);
			
			
		}
		
		
		
		// 2. 좋아요가 해제된 상태일 경우(likeCheck == 0) 누르면 체크 상태로 변경(BOARD_LIKE 테이블에 INSERT)
		
		else {
			
			result = mapper.insertBoardLike(map);
		}
		
		
		// 3. 해당 게시글의 좋아요 개수를 다시 조회해서 즉시 반환
		
		
		if(result > 0) {
			
			// map에 있는 boardNo를 가지고 감
			return mapper.selectLikeCount(map.get("boardNo"));
		}
		
		// 좋아요 개수 관련 오류를 알아내기 위해 -1로 설정(확실히 구별하기)
		return -1;
		
		
		
	}
	
	
	
	
	
	
	/**
	 * 조회수의 기본 개념 생각하기
	 * 
	 * 1. 페이지를 조회할 때마다 증가(새로고침에 취약)
	 * 2. DB에 누가 어떤 글을 조회했는가 일정 기간 단위로 확인해서 증가
	 * 3. local/session 스토리지 이용(JS에서만 사용 가능)
	 * 4. * 쿠키를 이용한 조회 수 증가 *
	 * 5. In Memory DB (redis) 사용
	 */
	
	
	
	
	@Override
	public int updateReadCount(int boardNo) {
		
		// 1. 조회 수 1 증가
			int result = mapper.updateReadCount(boardNo);
				
			
		// 2. 현재 조회 수 조회
			if(result > 0) {
				
				return mapper.selectReadCount(boardNo);
				
			}
				
				return -1; // 실패한 경우 -1 반환
	
	}
	
	
	
	
	/**
	 * 검색 서비스 (+게시판 조회 재활용한 코드)
	 */
	@Override
	public Map<String, Object> searchList(Map<String, Object> paramMap, int cp) {
		
		
		
		// 1. 지정된 게시판애서 검색 조건에 맞으면서 삭제되지 않은 게시글 수를 조회
		int listCount = mapper.getSearchCount(paramMap);
		
		
		
		// 2. 1번의 결과 + cp 이용해서 Pagination 객체를 생성
				Pagination pagination = new Pagination(cp, listCount);
				
				
				
	
		// 3. 지정된 페이지의 검색 결과 목록 조회
				
				
				int limit = pagination.getLimit();
				int offset = (cp - 1) * limit;
				RowBounds rowBounds = new RowBounds(offset, limit);
				
				
				
				List<Board> boardList = mapper.selectSearchList(paramMap, rowBounds);
				
				
				
				// 4. 목록 조회한 결과 + pagination객체르 Map으로 묶음
				Map<String, Object> map = new HashMap<>();
				
				map.put("pagination", pagination);
				map.put("boardList", boardList);
				
				//  5. 결과 반환
				
				return map;
	}
	
	
}
