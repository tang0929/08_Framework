const totalCount = document.querySelector("#totalCount");
const completeCount = document.querySelector("#completeCount");
const reloadBtn = document.querySelector("#reloadBtn");
// ajax에서 할 일 추가 관련 요소들
const todoTitle = document.querySelector("#todoTitle"); 
const todoContent = document.querySelector("#todoContent"); 
const addBtn = document.querySelector("#addBtn"); 
const homeBtn = document.querySelector("#homeBtn");



// ---------------------------------------------------------------------------------




// 전체 Todo 개수 조회 및 출력하는 함수
function getTotalCount(){ // 함수 정의

  // 비동기로 서버(DB)에서 전체 Todo 개수 조회하는
  // fetch() API 코드 작성
  // (fetch : 가지고 오다)

  fetch("/ajax/totalCount") // 비동기 요청 수행 -> Promise객체 반환
  .then( response => {
    // response : 비동기 요청에 대한 응답이 담긴 객체

    // response.text() : 응답 데이터를 문자열/숫자 형태로 변환한
    //                  결과를 가지는 Promise 객체 반환

    console.log(response); 
    // console.log(response.text());

    return response.text();
  })

  // 두 번째 then의 매개 변수(result)
  // == 첫 번째 then에서 반환된 Promise 객체의 PromiseResult 값
  .then(result => {
    
    // result 매개 변수 == Controller 메서드에서 반환된 값
    console.log("result", result); 

    // #totalCount 요소의 내용을 result으로 변경
    totalCount.innerText = result;
  });

}

// getTotalCount()  // 함수 호출



// completeCount 값 비동기 통신으로 얻어와서 화면 출력
function getCompleteCount(){

  // fetch() : 비동기로 요청해서 결과 데이터 가져오기

  // 첫 번째 then의 response : 
  // - 응답 결과, 요청 주소, 응답 데이터 등이 담겨있음

  // response.text() : 응답 데이터를 text 형태로 변환

  // 두 번째 then의 result
  // - 첫 번째 then에서 text로 변환된 응답 데이터(completeCount값)

  fetch("/ajax/completeCount")
  .then( response => {return response.text()} )
  //  ==  .then( response => response.text()} )
  .then(result => {

    // #completeCount 요소에 내용으로 result값 출력
    completeCount.innerText = result;
  })
}





reloadBtn.addEventListener('click',() => {
    getTotalCount();  // 비동기로 전체 할 일 개수 조회
    getCompleteCount();  // 비동기로 완료된 할 일 개수 조회

});



homeBtn.addEventListener('click', () => {
    location.href="/";
});



// 할 일 추가 버튼 클릭
addBtn.addEventListener('click',() => {
    // 비동기로 할 일 추가하는 fetch() API 작성
    // - 요청 주소 : "/ajax/add"
    // - 데이터 전달 방식(method) : "POST"

    // 파라미터를 저장한 JS 객체
    const param = {"todoTitle" : todoTitle.value, "todoContent" : todoContent.value};

    fetch("/ajax/add", { 
        // key : value
        method : "POST",
        headers : {"Content-Type" : "application/json"},  // 요청 데이터의 형식(Type)을 JSON으로 지정
        body : JSON.stringify(param)
        // stringify() : ()을 JSON(String)으로 변환한다. (한마디로 문자열화)
    })

    .then(resp => resp.text()) // 반환된 값을 text로 변환

    .then(temp => {

        if(temp > 0) {
            alert("ajax로 추가 성공");

            // 추가 성공한 제목과 내용 지우기
            todoTitle.value = "";
            todoContent.value = "";
            // 할 일이 추가되었으므로 전체 Todo 개수를 다시 조회
            getTotalCount(); 
        } else {
            alert("ajax로 추가 실패");
        }
    }) // 첫번째 then에서 반환된 값 중 변환된 text를 temp에 저장
});




// js 파일에 함수 호출 코드 작성 -> 페이지 로딩 시 바로 실행
getTotalCount();
getCompleteCount();
