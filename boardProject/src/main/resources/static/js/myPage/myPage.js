
/* 회원 정보 수정 페이지 */
const updateInfo = document.querySelector("#updateInfo");


// updateInfo가 있을 때(=현재 수정페이지에 있을 때)에만 수행
if(updateInfo != null){

    // 제출하기 버튼 클릭시
    updateInfo.addEventListener("submit", e=>{

        const memberNickname = document.querySelector("#memberNickname");
        const memberTel= document.querySelector("#memberTel");
        // 배열 형태인 memberAddress를 모두 가져옴
        const memberAddress= document.querySelectorAll("[name='memberAddress']");

        
        // 닉네임 유효성 검사

        if(memberNickname.value.trim().length == 0) {
        // 닉네임이 입력되지 않은 경우
        alert("변경할 닉네임을 입력해주세요.")
        e.preventDefault(); // 제출 막기
        return;
        }


        let regExp = /^[가-힣a-zA-Z0-9]{2,10}$/;
        // 수정할 닉네임이 닉네임 유효성에 맞지 않은 경우
        if(!regExp.test(memberNickname.value)){
            alert("변경할 닉네임이 유효하게 작성되지 않습니다.");
            e.preventDefault();
            return;
        }



        // 중복검사(닉네임 중복안되도록 수정하게 조심)



        // 전화번호 유효성 검사
        if(memberTel.value.trim().length == 0) {
            // 전화번호가 입력되지 않은 경우
            alert("변경할 전화번호를 입력해주세요.")
            e.preventDefault(); // 제출 막기
            return;
            }
        

        regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;



         // 수정할 전화번호가 전화번호 유효성에 맞지 않은 경우
         if(!regExp.test(memberTel.value)){
            alert("변경할 전화번호가 유효하게 작성되지 않습니다.");
            e.preventDefault();
            return;
        }

        

        // 주소 유효성 검사


        // 주소는 우편번호/도로명주소/상세주소 3칸이 전부 없거나 전부 다 차 있어야한다.


        const addr0 = memberAddress[0].value.trim().length == 0; // 
        const addr1 = memberAddress[1].value.trim().length == 0; // 
        const addr2 = memberAddress[2].value.trim().length == 0; 
        // addr[]가 길이가 0이면 (=입력 안한상태) true, 입력이 뭐라도 되어있으면 false


        const result1 = addr0 && addr1 && addr2; 
        // addr[]이 모두 true인 경우에만 true로 저장됨 = 아무것도 입력을 안했을 때 true
        const result2 = !(addr0 || addr1 || addr2); 
        // addr[]가 하나라도 true이면 true가 저장됨 을 부정함
        // = 모두 다 입력을 하면 true


        // 3개 모두를 적은 상태이거나 모두 미입력이라면 
        if(!(result1 || result2)){
            alert("주소를 미입력하거나 모두 입력해주세요.");
            e.preventDefault();
        
        }
 

    });

}



/* 비밀번호 변경 페이지 */
/* 컨트롤러 : 현재/새 비밀번호 파라미터, 세션에서 회원 번호 얻어오기(서비스 호출)
서비스 : bcrypt 암호화된 비밀번호를 DB에서 조회(회원 번호가 필요함) 현재 비밀번호와 bcrypt 암호화 비밀번호 비교
        BcryptPasswordEncoder.matches(암호화 안된 평문,암호화된 비밀번호) 둘이 비교
        같으면 true, 새로 입력한 비밀번호로 변경(UPDATE)하는 mapper 호출 (mapper은 회원번호, 새 비밀번호를 받아오므로 하나로 묶기). 
        결과 1또는 0반환 
다시 컨트롤러 : 비밀번호 변경 성공시 alert로 변경 성공 + /myPage/info로 리다이렉트. 
        변경 실패시(비밀번호가 일치하지 않으면) alert하고 /myPage/changePw로 리다이렉트*/



// ----------------------------------------------------------------------------



// 비밀번호 변경 form 태그
const changePw = document.querySelector("#changePw");


// 비밀번호 변경 화면인 상태일때에만 해당 코드를 실행

if(changePw != null){
    changePw.addEventListener("submit", e =>{

        const currentPw = document.querySelector("#currentPw");
        const newPw = document.querySelector("#newPw");
        const newPwConfirm = document.querySelector("#newPwConfirm");

        // 값을 모두 입력했을때


        let str; // 단순히 변수만 설정하면 undefinded 상태

        if(currentPw.value.trim().length == 0){
            str = "현재 비밀번호를 입력하세요.";
        } else if(newPw.value.trim().length == 0) {
            str = "새 비밀번호를 입력하세요.";
        } else if(newPwConfirm.value.trim().length == 0){
            str = "새 비밀번호 확인을 입력하세요.";
        }

        // str에 무언가 대입이 되었을때 = undefinded가 아닐때
        if(str != undefined){
            alert(str);
            e.preventDefault();
            return;
        }


        // 새 비밀번호에 대한 정규식
        const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;


        if(!regExp.test(newPw.value)){
            // 새 비밀번호가 정규식에 맞지 않은 경우
            alert("새 비밀번호가 유효하지 않습니다.");
            e.preventDefault();
            return;
        }

        // 새 비밀번호가 비밀번호 확인과 일치한가 
        if(newPw.value != newPwConfirm.value){
            // 새 비밀번호와 비밀번호 확인이 서로 같지 않은경우
            alert("비밀번호 확인이 일치하지 않습니다.");
            e.preventDefault();
            return;
        }  
    }); 
}


//---------------------------------------------------------------------------------
/* 탈퇴 확인 여부  */
const secession = document.querySelector("#secession");


if(secession != null){

    secession.addEventListener("submit", e => {

        const memberPw = document.querySelector("#memberPw");
        const agree = document.querySelector("#agree");

        // 비밀번호가 입력되었는지 확인
        if(memberPw.value.trim().length == 0) {
            alert("비밀번호를 입력하세요.");
            e.preventDefault();
            return;
        }

        // 약관 동의가 체크가 되었는지 확인
        // .checked : 체크박스, radio가 체크되어있으면 true, 아니면 false 
        //     checked = true -> 체크하기.
        if(! agree.checked ){

            // 동의 여부가 체크가 안되어있을때
            alert("약관 동의여부를 체크하지 않았습니다.");
            e.preventDefault();
            return;
        }
        

        // 정말 탈퇴하겠냐고 물어보기
        if(!confirm("정말 탈퇴하시겠습니까?")){
            // 취소 선택시
            alert("취소되었습니다.");
            e.preventDefault();
            return;


        }
    })
}