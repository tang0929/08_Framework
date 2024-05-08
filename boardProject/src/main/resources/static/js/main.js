/* 쿠키에서 key가 일치하는 value 얻어오는 함수 */

// 쿠키는 "K=V; K=V..." 형식이 반복

const getCookie = key => {

    const cookies = document.cookie; // "K=V; K=V"

    // cookies 문자열을 배열 형태로 변환
    const cookieList = cookies.split("; ")
    // cookies가 ; 문자를 기준으로 쪼개짐
                            .map(el => el.split("=")); // 
   // console.log(cookieList);
    // 배열.map(함수) : 배열의 각 요소를 이용해 함수 수행 후 결과 값으로 새로운 배열을 만들어서 반환


    // 배열 -> 객체로 변환(다루기가 쉬워짐)
    const obj = {};
    for(let i = 0 ; i < cookieList.length ; i++){
        const k = cookieList[i][0];
        const v = cookieList[i][1];
        obj[k] = v; // 객체에 추가로 객체가 됨
    }
    return obj[key]; 
}
    //console.log("obj",obj);
    
    // 매개 변수로 전달받은 key와 obj객체에 저장된 키가 일치하는 요소의 값 반환
    const loginEmail = document.querySelector("#loginForm input[name='memberEmail']")
    // 아이디가 loginForm인 요소중에서 이름이 memberEmail인 요소를 찾기


    // 로그인 안된 상태에서만 수행
    if(loginEmail != null){
        // 로그인창의 이메일 입력 부분이 있을 때
        // 쿠키 중 key값이 "saveId"인 요소의 value 얻어오기
        const saveId = getCookie("saveId");  // undefined 또는 memberEmail을 얻어옴


        // saveId값이 있을경우 == 체크되어있을 경우
        if (saveId != undefined) {
            loginEmail.value = saveId; // 쿠키에서 얻어온 값을 input에 value로 세팅(=재입력하지 않아도 그대로 출력되어있음)
            document.querySelector("input[name='saveId']").checked = true; // 아이디 저장 체크박스를 그대로 체크 상태로 두기
        } 
    }


    /* 아이디, 비밀번호 둘 중 하나라도 입력되지 않은 상태에서 제출하려고 한다면
     form 요소에서 submit 이벤트가 발생되면 e.preventDefault시키기 */


    const loginForm = document.querySelector("#loginForm");
    // const loginPw = document.querySelector("#loginForm input[name='memberPw']");

    // #loginForm이 화면에 존재할 때(=로그인 하기 전)
    if (loginForm != null){

        loginForm.addEventListener("submit",e => {

            if(document.querySelector("#memberEmail").value.trim().length == 0){
                alert("ID를 입력해주십시오.");
                e.preventDefault();
                return;
            }
            
            if(document.querySelector("#memberPw").value.trim().length == 0){
                alert("비밀번호를 입력해주십시오.");
                e.preventDefault();
                return;
            }
        })

    }



    /* 빠른 로그인 */
    const quickLoginBtns = document.querySelectorAll(".quick-Login");


    // 배열에는 이벤트를 추가할 수 없다. (quickLoginBtns.addEventListener 사용 불가)
    // 배열 요소를 하나씩 꺼내서 이벤트 리스너에 추가
    quickLoginBtns.forEach((item,index) => {
        // item : 현재 반복시 꺼내온 객체
        // index : 현재 반복중인 인덱스

        item.addEventListener("click", e =>{

            const email = item.innerText; // 버튼에 작성된 이메일 얻어오기

            location.href = "/member/quickLogin?memberEmail=" + email;

        });

    });


    /* 회원 목록 조회 */

    const selectMemberList = document.querySelector("#selectMemberList");

    const memberList = document.querySelector("#memberList");


    // td 요소를 만들고 text 추가 후 반환
const createTd = (text) => {
    const td = document.createElement("td");
    td.innerText = text;
    return td;
  }

    selectMemberList.addEventListener("click",() =>{
        // 비동기로 회원 목록 조회 (포함되어야 하는 회원 정보 : 회원번호, 이메일, 닉네임, 탈퇴여부)
        fetch("/member/selectMemberList")

        // 첫번째 then(response => response.json())
        /* JSON ARRAY -> JS 객체 배열로 변환 [{},{},{},{}] */
        .then(response => response.json())
        // 두번째 then
        /* tbody에 이미 작성되어 있던 내용(이전에 조회한 목록) 삭제 후 조회된 JS 객체 배열을 이용해 tbody에 들어갈
        요소를 만들고 값 세팅 후 추가*/

        .then(list => {
            console.log(list);

            memberList.innerHTML = "";
            
              // tbody에 들어갈 요소를 만들고 값 세팅 후 추가
            list.forEach( (member, index) => {
            // member : 반복 접근한 요소(순서대로 하나씩 꺼낸 요소)
            // index : 현재 접근 중인 index
  
            // tr 만들어서 그 안에 td 만들어서 append 후
            // tr을 tbody에 append
  
            const keyList = ['memberNo', 'memberEmail', 'memberNickname', 'memberDelFl'];
  
            const tr = document.createElement("tr"); 
  
            // keyList에서 key를 하나씩 얻어온 후
            // 해당 key에 맞는 member 객체 값을 얻어와
            // 생성되는 td 요소에 innerText로 추가 후
            // tr요소의 자식으로 추가
            keyList.forEach( key => tr.append( createTd(member[key]) ) );
        
            // tbody 자식으로 tr 추가
            memberList.append(tr);
        })
    });
        



    });



    // 특정 회원의 비밀번호 초기화 기능
    const resetMemberNo = document.querySelector("#resetMemberNo");
    const resetPw = document.querySelector("#resetPw");

    resetPw.addEventListener("click",() => {

        // 입력받은 회원번호 얻어오기
        const inputNo = resetMemberNo.value;
        // 입력한 회원번호가 없으면 못함
        if(inputNo.trim().length == 0){
            alert("회원번호를 먼저 입력해주세요");
            return;
        }



        fetch("/resetPw",{
            method : "PUT", // 수정 요청 방식
            headers : {"Content-Type" : "application/json"},
            body : inputNo

        })
        .then(resp => resp.text())
        .then(result =>{
            // result는 Controller에서 반환받은 값, 이를 Text로 파싱한 값


            if(result > 0){
                alert("비밀번호 초기화 성공");
            }else {
                // 입력한 회원번호가 없을경우
                alert("초기화할 회원 번호가 존재하지 않습니다.");
            }
        });

    });



    // 특정 회원 탈퇴 복구
    const resetMemberNo2 = document.querySelector("#resetMemberNo2");
    const resetSecession = document.querySelector("#resetSecession");

    resetSecession.addEventListener("click",() => {

        // 입력된 회원번호를 얻어옴
        const inputNo2 = resetMemberNo2.value;

        // 입력된 회원번호가 없을경우
        if(inputNo2.trim().length == 0){
            alert("탈퇴 복구할 회원번호를 입력해주세요.");
            return;
        }

        fetch("/resetSecession",{
            method : "PUT",
            headers : {"Content-Type" : "application/json"},
            body : inputNo2
        })
        .then(resp => resp.text())
        .then(result => {
            
            if(result > 0) {
                alert("해당 회원의 탈퇴가 복구되었습니다.");
            } else {
                alert("탈퇴를 복구할 회원이 존재하지 않습니다.");
            }

        });
    }); 


    /* 로그인상태에서 마이페이지 버튼 활성화 */
    const myPageBtn = document.querySelector("#myPageBtn");

    myPageBtn.addEventListener("click",() => {
        location.href ="/myPage/info"
    })