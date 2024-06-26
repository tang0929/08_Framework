

/* 글쓰기 버튼 관련 동작 */

const insertBtn = document.querySelector("#insertBtn");


// 글쓰기 버튼이 있을 때 == 로그인 상태일시
if(insertBtn != null){

    insertBtn.addEventListener('click',() => {


        /* boardCode 얻어오기

        1. location.pathname.split("/")[2]
        2. @PathVariable("boardCode") 얻어와 전역 변수 저장 */

        
        // get방식 요청
        location.href = `/editBoard/${boardCode}/insert`;
    })
}







/* 검색 관련 요소 */
const searchKey = document.querySelector("#searchKey");
const searchQuery = document.querySelector("#searchQuery");
const options = document.querySelectorAll("#searchKey > option");


/* 하단 검색창에서 검색을 하고 화면이 바뀌면, 검색한 내역이 그대로 남게 하는 기능 */



// 즉시 실행 함수
// (() => {})();

// 함수가 정의되자마자 바로 실행함
/* 변수명 중복을 해결, 함수를 선언하고 호출하는데 최소 2줄이 필요한 상황을 풀어주고 속도가 상승함 */


// 쿼리스트링 값을 key, value 구분해서 저장하는 객체 반환
(()=>{
    const params = new URL(location.href).searchParams;
  
    const key = params.get("key"); // t, c, tc, w 중 하나
    const query = params.get("query"); // 검색어
  
    if(key != null){ // 검색을 했을 때
        searchQuery.value = query; // 검색어를 화면에 출력
  
        // option태그를 하나씩 순차 접근해서 value가 key랑 같으면
        // selected 속성 추가 
        for(let op of options){
            if(op.value == key){
                op.selected = true;
            }
        }
    }
}
)