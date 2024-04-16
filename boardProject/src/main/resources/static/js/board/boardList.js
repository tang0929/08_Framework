

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