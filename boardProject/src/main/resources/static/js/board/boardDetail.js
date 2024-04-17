
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


/* 삭제 버튼 클릭시 게시글 삭제 */ 

const deleteBtn = document.querySelector("#deleteBtn");

deleteBtn.addEventListener("click", () => {

    if(confirm("정말 삭제하시겠습니까?")){


        location.href = "/editBoard/"+boardCode+"/"+boardNo+"/delete";



        return;

    } else {

        alert("삭제 취소");
        return;
    }
})


/* POST 방식은 form을 사용함

POST 방식은 ajax 또한 사용할 수 있지만 비동기를 사용할 것이 아니므로 쓰지않음


if(deleteBtn2 != null){
  deleteBtn2.addEventListener("click", () => {

    if( !confirm("삭제 하시겠습니까?") ) {
      alert("취소됨")
      return;
    }

    const url = location.pathname.replace("board","editBoard")  + "/delete"; 

    // form태그 생성
    const form = document.querySelector("form");
    form.action = url;
    form.method = "POST";

    // cp값을 저장할 input 생성
    const input = document.querySelector("input");
    input.type = "hidden";
    input.name = "cp";

    // 쿼리스트링에서 원하는 파라미터 얻어오기
    const params = new URLSearchParams(location.search)
    const cp = params.get("cp");
    input.value = cp;

    form.append(input);

    // 화면에 form태그를 추가한 후 제출하기
    document.querySelector("body").append(form);
    form.submit();
  });
}

*/




/* 게시글 수정버튼 동작 */
/* 수정 버튼은 수정버튼이 활성화 되어있을때에만 사용 가능 */
if(updateBtn != null){

    const updateBtn = document.querySelector("#updateBtn");

    updateBtn.addEventListener("click", () => {

        // /editBoard/1/2010/update?cp=1 GET 방식 요청

        // location 현재 경로인 /board/1/2010?cp=1에서 board를 editBoard로, 바꾼 url로 이동
        location.href = location.pathname.replace('board','editBoard') + "/update" + location.search;
    })

}

