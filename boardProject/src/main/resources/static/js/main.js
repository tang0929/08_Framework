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