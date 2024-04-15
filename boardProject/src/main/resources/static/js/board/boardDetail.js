
/* 좋아요 버튼(하트) 클릭시 비동기로 좋아요 삭제/추가 */


// 1) 로그인한 회원 번호를 준비 --> session에서 얻어오기.
// 근데 session은 서버에서 관리하기 때문에 JS에서 바로 못얻어옴




// 2) 현재 게시글 번호 준비

/* Thymeleaf(Template Engine) : html 코드(+css, js)와 th(java + Spring EL) 코드로 이루어져있음 

Thymeleaf 코드 해석 순서 : th -> html */


// 3) 좋아요 준비 여부



// 1. #boardLike 가 클릭되었을 때
const boardLike = document.querySelector("#boardLike");
boardLike.addEventListener("click",e=>{

    // 2. 로그인 상태가 아닌 경우
    if(loginMemberNo == null){
        alert("로그인 후 이용해주세요")
        return;
    }


    // 3. 준비된 3개의 변수를 객체로 저장(JSON으로 변환 예정)
    const obj = {
        "memberNo" : loginMemberNo,
        "boardNo" : boardNo,
        "likeCheck" : likeCheck
    };


    // 4. 좋아요 INSERT/DELETE 비동기 요청


    fetch("/board/like", {
        method : "POST",
        headers : {"Content-Type" : "application/json"},
        body : JSON.stringify(obj)
    })
    .then(resp => resp.text()) // 반환 결과 text(글자) 형태로 변환
    .then(count => {
        // count == 첫 번째 then이 파싱되어 반환된 값 -1 또는 '게시글 좋아요 수'가 나옴
        // console.log("result : ", result);


        if(count == -1){
            console.log("좋아요 처리 실패");
            return;
        }

        // 5. likeCheck 값 0 <-> 1이 왔다갔다하도록 반환.
        // 클릭될때마다 INSERT/DELETE 동작을 번갈아 가면서 할 수 있기 때문

        likeCheck = likeCheck == 0 ? 1 : 0;


        // 6. 하트를 채웠다가 비웠다가 바꾸는 기능
        e.target.classList.toggle("fa-regular"); // 빈하트
        e.target.classList.toggle("fa-solid"); // 꽉찬하트



        // 7. 게시글 좋아요 개수 수정
        e.target.nextElementSibling.innerText = count;

    });
});