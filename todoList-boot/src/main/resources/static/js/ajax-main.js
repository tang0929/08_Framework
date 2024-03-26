const totalCount = document.querySelector("#totalCount");
const completeCount = document.querySelector("#completeCount");
const reloadBtn = document.querySelector("#reloadBtn");
// ajax에서 할 일 추가 관련 요소들
const todoTitle = document.querySelector("#todoTitle"); 
const todoContent = document.querySelector("#todoContent"); 
const addBtn = document.querySelector("#addBtn"); 
const homeBtn = document.querySelector("#homeBtn");
// 할 일 목록 조회관련 요소
const tbody = document.querySelector("#tbody");
// 할 일 상세조회 관련 요소들
const popupLayer = document.querySelector("#popupLayer");
const popupTodoNo = document.querySelector("#popupTodoNo");
const popupTodoTitle = document.querySelector("#popupTodoTitle");
const popupComplete = document.querySelector("#popupComplete");
const popupTodoContent = document.querySelector("#popupTodoContent");
const popupRegDate = document.querySelector("#popupRegDate");
// 버튼 
const popupClose = document.querySelector("#popupClose");
const deleteBtn = document.querySelector("#deleteBtn");
const changeComplete= document.querySelector("#changeComplete");
const updateView = document.querySelector("#updateView");

// 수정
const updateLayer = document.querySelector("#updateLayer");
const updateTitle = document.querySelector("#updateTitle");
const updateContent = document.querySelector("#updateContent");
const updateBtn = document.querySelector("#updateBtn");
const updateCancel = document.querySelector("#updateCancel");




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

            // 할 일 목록도 다시 조회
            selectTodoList();
        } else {
            alert("ajax로 추가 실패");
        }
    }) // 첫번째 then에서 반환된 값 중 변환된 text를 temp에 저장
});




// 비동기(ajax)로 할 일 상세 조회하는 함수
const selectTodo = (url) => {

// 매개변수 url = "/ajax/detail?todoNo=.." 형태의 문자열

// response.json() : 응답 데이터가 JSON일 경우 이를 자동으로 Object 형태로 변환하는 메서드

// == JSON.parse
  fetch(url)
  .then(response => response.json())
  .then(todo => {
    // 매개 변수 todo : 
    // 서버 응답이 Object로 변환된 값. 
    console.log(todo);
    
    /* popup layer에 조회된 값을 출력 */
    popupTodoNo.innerText = todo.todoNo; 
    popupTodoTitle.innerText = todo.todoTitle; 
    popupComplete.innerText = todo.complete; 
    popupRegDate.innerText = todo.regDate; 
    popupTodoContent.innerText = todo.todoContent; 
   

    // popupLayer를 보이게 함
    popupLayer.classList.remove("popup-hidden");


    // updateLayer가 열려있는 상황일때 popupLayer를 닫게 하기
    updateLayer.classList.add("popup-hidden");


    /* 요소.classList.toggle("클래스명") 
     - 요소에 해당 클래스가 있으면 제거하고 없으면 추가함(기계 스위치같은 역할을 함)

     요소.classList.add("클래스명")   <-> remove
     - 요소에 해당 클래스가 없으면 추가 <-> 있으면 제거
    */


  });
};




// -----------------------------------------------------------
// 비동기(ajax)로 할 일 목록을 조회하는 함수 만들기
const selectTodoList = () => {

  fetch("/ajax/selectList")

  // 응답 결과를 text형식으로 변환시킴
  .then(response => response.text())

  .then(result => {
    console.log(result)
    console.log(typeof result) // 타입 검사 결과로 String이 출력됨 -> JSON은 객체가 아니라 문자열이다!

    // JSON.parse(JSON데이터) : String 형태인 JSON데이터를 JS Object 타입으로 변환시킴
    // JSON.stringify(JS Object) : JS Object를 JSON 데이터로 변환시킴.
    // 서로 반대에 위치한 개념

    const todoList = JSON.parse(result);

    console.log(todoList); // 객체 배열 형태를 확인함






    /* 기존에 출력되어 있던 할 일 목록을 모두 삭제 */
    tbody.innerHTML = "";

    // #tbody에 tr td를 생성하고 내용을 추가

    for(let todo of todoList){
      // tr 태그 생성
      const tr = document.createElement("tr");

      const arr = ['todoNo','todoTitle','complete','regDate'];
      
      for(let key of arr) {
        const td = document.createElement("td")
        if(key === 'todoTitle'){
          const a = document.createElement("a"); // todoTitle에 a태그 생성
          a.innerText = todo[key]; // 제목을 a태그 내용으로 대입
          a.href = "/ajax/detail?todoNo="+todo.todoNo;
          td.append(a);
          tr.append(td);

          // a태그 클릭시 발생하는 이벤트가 일어나지 않게 함
          a.addEventListener("click",e => {
            e.preventDefault()
            selectTodo(e.target.href);
          });
          continue;
        }
          
        td.innerText = todo[key]; 
        tr.append(td);
      }

      // const td1 = document.createElement("td");
      // td1.innerText = todo.todoNo;
      // const td2 = document.createElement("td");
      // td2.innerText = todo.todoTitle;
      // const td3 = document.createElement("td");
      // td3.innerText = todo.complete;
      // const td4 = document.createElement("td");
      // td4.innerText = todo.regDate;

      // tr.append(td1, td2, td3, td4); // tr의 자식으로 추가


      // tbody의 자식으로 tr(한 줄)추가
      tbody.append(tr);

    }
  })
};


/* popClose의 X버튼 클릭시 창 닫기 */
popupClose.addEventListener("click",() =>{
  popupLayer.classList.add("popup-hidden");
});




/* popupLayer의 삭제 버튼 */
deleteBtn.addEventListener("click",()=>{
  if(!confirm("정말 삭제하시겠습니까?"))
  // 정말 삭제하시겠습니까? 질문창에서 취소를 눌렀을 때
  {return;} 
  else {
    // 삭제할 할 일 번호를 얻어옴
    const todoNo = popupTodoNo.innerText;  // #popupTodoNo 내용을 얻어옴

    // 비동기 delete 방식 요청
    fetch("/ajax/delete", {
      method : "DELETE", // DELETE 방식 요청 -> @DeleteMapping 처리
      headers : {"Content-type" : "application/json"}, // 전달받을 데이터가 text임
      body : todoNo // todoNo값을 body에 담아서 전달 
       // -> @RequestBody로 꺼냄
    })
    .then(response => response.text()) // 요청 결과를 text(문자열, 숫자 포함)형식으로 반환받음
    .then(result => {
      if (result > 0) {
        // 삭제가 되었을 경우
        alert("삭제되었습니다.");
        // 상세조회창 닫기
        popupLayer.classList.add("popup-hidden");
        // 전체 완료된 개수 + 완료된 할 일 개수 + 할 일 목록하기
        getTotalCount();
        getCompleteCount();
        selectTodoList();

      }else {
        alert("삭제 실패");
      }
    })

  }
});




/* 완료 여부 변경 버튼 클릭 시 */
changeComplete.addEventListener('click', e => {

  // 변경할 할 일 번호, 완료 여부(Y <-> N)
  const todoNo = popupTodoNo.innerText;
  const complete = popupComplete.innerText === 'Y' ? 'N' : 'Y'; 

  // SQL 수행에 필요한 값을 객체로 묶음
  const obj = {"todoNo" : todoNo,  "complete" : complete};

  // 비동기로 완료 여부 변경
  fetch("/ajax/changeComplete", {
    method : "PUT", 
    headers : {"Content-Type" : "application/json"}, 
    body : JSON.stringify(obj) // obj를 JSON으로 변경
  })

  .then(response => response.text())
  .then(result => {

    if(result > 0){
      
      // selectTodo();  
      // 서버 부하를 줄이기 위해 상세 조회에서 Y/N만 바꾸기
      popupComplete.innerText = complete;
      //getCompleteCount();
      // 서버 부하를 줄이기 위해 완료된 Todo 개수 +-1

      const count = Number(completeCount.innerText);

      if(complete === 'Y') 
      completeCount.innerText = count + 1;
      else               
       completeCount.innerText = count - 1;


      selectTodoList();

    }else{
      alert("완료 여부 변경 실패");
    }
  })

});











// 상세 조회에서 수정버튼(#updateView) 클릭 시
updateView.addEventListener("click",() => {

  // 기존 팝업 레이어(상세조회)는 숨김
  popupLayer.classList.add("popup-hidden");



  // 수정 레이어가 보여짐
  updateLayer.classList.remove("popup-hidden");


  // 수정 레이어가 보여질 때 팝업 레이어에 있던 제목과 내용을 얻어와 미리 세팅해놓기
  updateTitle.value = popupTodoTitle.innerText;
  updateContent.value = popupTodoContent.innerHTML.replaceAll("<br>","\n");
  /* HTML 화면에서는 줄바꿈이 <br>로 인식되고 있는데 textarea에선 \n로 바꾸어야 줄 바꿈으로 인식된다 */



  // 수정 레이어의 수정 버튼에 data-todo-no 속성을 추가
  updateBtn.setAttribute("data-todo-no",popupTodoNo.innerText);
})





// 수정 레이어에서 취소 버튼 클릭시 수정 화면 닫고 다시 상세 조회 화면을 켜게 함
updateCancel.addEventListener("click",() => {
  updateLayer.classList.add("popup-hidden");
  popupLayer.classList.remove("popup-hidden");
})





// 수정 레이어 수정 버튼을 클릭 시 동작
updateBtn.addEventListener("click", e => {

  // 서버로 전달해야 하는 값을 객체로 묶음
  const obj = {"todoNo" : e.target.dataset.todoNo,
               "todoTitle" : updateTitle.value,
              "todoContent" : updateContent.value};

  fetch("/ajax/update",{
    method : "PUT", // 수정은 메서드가 PUT
    headers : {"Content-Type" : "application/json"},
    body : JSON.stringify(obj)
  })
  .then(response => response.text())
  .then(result => {
    if(result > 0) {
      alert("수정 완료");
      // 수정 레이어 닫기(숨기기)
      updateLayer.classList.add("popup-hidden");
      // 수정창에 입력되어 있던 흔적 제거
      updateTitle.value = "";
      updateContent.value = "";
      updateBtn.removeAttribute("data-todo-no");
      selectTodoList();


      // selectTodo(); -> 서버 부하를 막기 위한 성능개선
      /* popupTodoTitle.innerText = updateTitle.value;
      popupTodoContent.innerText = updateContent.value;
      
      popupTodoContent.innerHtml = updateContent.value.replaceAll("\n",<br>)
      
      popupLayer.classList.remove("popup-hidden");*/



    } else {
      alert("수정 실패");
    }
  }) 
  
    



  
})









// -------------------------------------------------------------------------
// js 파일에 함수 호출 코드 작성 -> 페이지 로딩 시 바로 실행
getTotalCount();
getCompleteCount();
selectTodoList();
