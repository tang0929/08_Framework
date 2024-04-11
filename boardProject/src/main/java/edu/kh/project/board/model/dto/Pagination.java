package edu.kh.project.board.model.dto;



/*
 * Pagination : 목록을 일정 페이지로 분할해서 원하는 페이지를 볼 수 있게 하는 것 == 페이징 처리
 * 
 * Pagination 객체 :페이징 처리에 필요한 값을 모여두고 계산하는ㄴ 객체}
 */
public class Pagination {
	
	   private int currentPage;      // 현재 페이지 번호
	   private int listCount;         // 전체 게시글 수
	   
	   private int limit = 10;         // 한 페이지 목록에 보여지는 게시글 수
	   private int pageSize = 10;      // 보여질 페이지 번호 개수
	   
	   private int maxPage;         // 마지막 페이지 번호
	   private int startPage;         // 보여지는 맨 앞 페이지 번호 
	   private int endPage;         // 보여지는 맨 뒤 페이지 번호
	   
	   private int prevPage;         // 이전 페이지 모음의 마지막 번호 
	   private int nextPage;         // 다음 페이지 모음의 시작 번호 
	   
	   
	   
	   
	public Pagination(int currentPage, int listCount) {
		super();
		this.currentPage = currentPage;
		this.listCount = listCount;
		
		calculate(); // 필드 계산 메서드 호출
	}


	public Pagination(int currentPage, int listCount, int limit, int pageSize) {
		super();
		this.currentPage = currentPage;
		this.listCount = listCount;
		this.limit = limit;
		this.pageSize = pageSize;
		
		calculate(); // 필드 계산 메서드 호출

	}


	public int getCurrentPage() {
		return currentPage;
	}


	public int getListCount() {
		return listCount;
	}


	public int getLimit() {
		return limit;
	}


	public int getPageSize() {
		return pageSize;
	}


	public int getMaxPage() {
		return maxPage;
	}


	public int getStartPage() {
		return startPage;
	}


	public int getEndPage() {
		return endPage;
	}


	public int getPrevPage() {
		return prevPage;
	}


	public int getNextPage() {
		return nextPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		
		calculate(); // 필드 계산 메서드 호출

	}


	public void setListCount(int listCount) {
		this.listCount = listCount;
		
		calculate(); // 필드 계산 메서드 호출

	}


	public void setLimit(int limit) {
		this.limit = limit;
		
		calculate(); // 필드 계산 메서드 호출

	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		
		calculate(); // 필드 계산 메서드 호출

	}

	
	
	
	@Override
	public String toString() {
		return "Pagination [currentPage=" + currentPage + ", listCount=" + listCount + ", limit=" + limit
				+ ", pageSize=" + pageSize + ", maxPage=" + maxPage + ", startPage=" + startPage + ", endPage="
				+ endPage + ", prevPage=" + prevPage + ", nextPage=" + nextPage + "]";
	}
	
	
	/**
	 * 페이징 처리에 필요한 값을 계산해서 필드에 대입하는 메서드
	 * (maxPage, startPage, endPage,  prevPage, nextPage)
	 */
	private void calculate() {
		
		
		// maxPage : 최대 페이지 == 마지막 페이지 == 총 페이지 수 
		// 한 페이지에 게시글이 10개씩 보여질 경우
		// ex)게시글 95개 : 10페이지
		// 게시글 100개 : 10페이지
		// 게시글 105개 : 11페이지
		
		// 게시글 수에 따라 알맞은 수의 maxPage가 나오는 수식 작성(ceil : 올림)
		maxPage = (int)Math.ceil((double)listCount/limit);
		
		
		
		// startPage : 페이지 번호 목록의 시작 번호
		// 페이지 번호 목록이 10(=pageSize)개씩 보여줄 경우 ex) 현재 페이지가 1 ~ 10 : 1 Page / 11 ~ 20 : 11 Page
		startPage = (currentPage - 1) / pageSize * pageSize + 1;
		
		
		// endPage : 페이지 번호 목록의 끝 번호
		// ex) 현재 페이지가 1~10 : 10 Page / 11 ~ 20 : 20 Page
		endPage = pageSize - 1 + startPage;
		
		
		// 출력할 maxPage가 도중에 끝나서 페이지 끝번호가 최대 페이지를 초과하는 경우 : 최대페이지를 끝번호로 설정함
		if(endPage > maxPage) endPage = maxPage;
		
		
		// prevPage : "<" 클릭 시 이동할 페이지 번호(이전 페이지 목록의 끝 번호로 지정하도록(이동하도록) 함)
		// ex) 41~50 에서 < 클릭시 40으로 이동
		if(currentPage < pageSize) {
			
			// 뒤로갈 페이지가 없는 1~10로 이동하게 되는 경우
			prevPage = 1;
			
			
		} else {
			prevPage = startPage -1; // 이미 계산한 startPage를 활용
		}
		
		
		
		// nextPage : ">" 클릭시 이동할 페이지 번호(이후 페이지 목록의 시작 번호로 지정하도록(이동하도록) 함)
		// ex) 11~20에서 > 클릭 시 21로 이동
		
		if(endPage == maxPage) {
			
			// 이후 넘어갈 페이지가 없을 경우
			nextPage = maxPage;
			
		}else {
			
			nextPage = endPage + 1; // 이미 계산한 endPage를 활용
			
		}
		
		
		
		
	}
	
	
	
	   
	
	
	   

}
