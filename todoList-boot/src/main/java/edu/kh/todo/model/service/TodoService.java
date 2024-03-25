package edu.kh.todo.model.service;

import java.util.Map;

import edu.kh.todo.model.dto.Todo;

public interface TodoService {
	
	/**
	 * 할 일 목록 + 완료된 할 일 개수 조회
	 * @return
	 */
	// Spring에서 발생하는 예외들은 대부분 UnChecked 상태이기에 별도의 예외 처리 필요 없음
	Map<String, Object> selectAll();

	
	
	/**
	 * 할 일 추가
	 * @param todoTitle
	 * @param todoContent
	 * @return
	 */
	int addTodo(String todoTitle, String todoContent);


	/**
	 * 할 일 상세 조회
	 * @param todoNo
	 * @return todo
	 */
	Todo todoDetail(int todoNo);



	/**
	 * 할 일 삭제
	 * @param todoNo
	 * @return
	 */
	int deleteTodo(int todoNo);


	/**
	 * 할 일 수정
	 * @param todo
	 * @return
	 */
	int todoUpdate(Todo todo);



	/**
	 * 완료 여부 변경
	 * @param todo
	 * @return
	 */
	int changeComplete(Todo todo);



	/**
	 * 전체 할 일 개수
	 * @return
	 */
	int getTotalCount();



	/**
	 * 
	 * @return
	 */
	int getCompleteCount();
	

}
