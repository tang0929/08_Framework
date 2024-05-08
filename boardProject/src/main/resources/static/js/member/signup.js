
/* 다음 주소 API를 활용함(복붙) */

function execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

           

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('postcode').value = data.zonecode;
            document.getElementById("address").value = addr;
            // 커서를 상세주소 필드로 이동한다.
            document.getElementById("detailAddress").focus();
        }
    }).open();
}


/* 주소 버튼 클릭시 실행되도록 변경*/
document.querySelector("#searchAddress").addEventListener("click",execDaumPostcode);




/* -------------------------------------------------------------------------------------- */

/* 회원가입 유효성 검사 */



// 필수 입력 항목의 유효성 검사 여부를 체크하기 위한 객체
// true -> 해당 항목은 유효한 형식으로 작성됨 <-> false
// 항목이 모두 true여야만 회원가입이 가능
const checkObj = {
    "memberEmail" : false,
    "memberPw" : false,
    "memberPwConfirm" : false,
    "memberNickname" : false,
    "memberTel" : false,
    "authkey" : false
};


/* 이메일 유효성 검사하기 */
// 1. 이메일 유효성 검사에 사용될 요소 얻어오기
const memberEmail = document.querySelector("#memberEmail");
const emailMessage = document.querySelector("#emailMessage"); // 이메일 유효성 여부를 확인해주는 메시지


// 2. 이메일이 입력(input 이벤트)이 될 때마다 유효성 검사 수행
memberEmail.addEventListener("input",e => {
    const inputEmail = e.target.value; // 작성된 email을 변수로 선언하고 값을 얻어옴


    // 이메일 인증을 성공하고 이메일이 변경된 경우
    checkObj.authkey = false;
    document.querySelector("#authKeyMessage").innerText = "";


    // 3. 공백을 제거하고 입력된 이메일이 없을 경우
    if(inputEmail.trim().length === 0){
        
        emailMessage.innerText = "메일을 받을 수 있는 이메일을 입력해주세요.";


        // 메시지에 색상을 추가하는 클래스를 모두 제거(=검정색으로 만듬)
        emailMessage.classList.remove('confirm','error');


        // 이메일 유효성 검사를 false로 선언
        checkObj.memberEmail = false;


        // 잘못 입력한 띄어쓰기가 있을 경우 없앰(=처음부터 띄어쓰기하는 경우를 막음)
        memberEmail.value = "";


        return; // 아래에 있는 유효성 검사를 실행하지 않고 그대로 끝냄 
    }

    
    // 4. 입력된 이메일이 있을 경우 유효성 검사(알맞은 형태로 작성했는가?)

    const regExp = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    // 입력받은 이메일이 정규식과 일치하지 않는경우(=알맞은 이메일 형태가 아닌경우)

    if(!regExp.test(inputEmail)){
        emailMessage.innerText = "알맞은 이메일 형식이 아닙니다.";

        emailMessage.classList.add('error'); // 메시지를 css에서 작성한 'error'형태로 설정함(빨간 글씨)

        emailMessage.classList.remove('confirm'); 

        checkObj.memberEmail = false;
        return;
    } 


    // 5. 유효한 형식으로 작성된 이메일을 중복 검사 수행
    // 비동기형식 fetch 사용
    fetch("/member/checkEmail?memberEmail="+ inputEmail)
    .then(response => response.text())
    .then(count => {

        // count : 중복검사를 한 결과 1이면 중복이므로 유효 안함.
        if(count == 1){
            emailMessage.innerText = "이미 가입한 이메일입니다."

            emailMessage.classList.add('error');

            emailMessage.classList.remove('confirm');

            checkObj.memberEmail=false;

            return;
        } 
        // count : 검사 결과 0이면 중복이 아니므로 유효함
        if(count == 0){
            emailMessage.innerText = "사용 가능한 이메일입니다."

            emailMessage.classList.add('confirm');

            emailMessage.classList.remove('error');

            checkObj.memberEmail = true;

        }
    })
    .catch( e => {
        // fetch() 수행 중 오류 발생시 처리하는 부분
        console.log(e) // 발생한 예외 출력
    })



} );





/* 비밀번호 / 비밀번호 확인 유효성 검사 */

const memberPw = document.querySelector("#memberPw");
const memberPwConfirm = document.querySelector("#memberPwConfirm");
const pwMessage = document.querySelector("#pwMessage");


// 비밀번호, 비밀번호 확인이 같은지 검사하는 함수
const checkPw = () => {
    if(memberPw.value === memberPwConfirm.value){
    // 비밀번호와 비밀번호 확인이 같게 입력된 경우

    pwMessage.innerText = "유효한 비밀번호가 서로 같게 입력되었습니다."

    pwMessage.classList.add("confirm");

    pwMessage.classList.remove("error");

    checkObj.memberPwConfirm = true; 

    return;
}
    pwMessage.innerText = "비밀번호가 불일치합니다."

    pwMessage.classList.add("error");

    pwMessage.classList.remove("confirm");

    checkObj.memberPwConfirm = false; 

}



// 1. 비밀번호가 입력(input)이 되어있는지 확인
memberPw.addEventListener("input",e => {
    const inputPw = e.target.value // 입력받은 비밀번호 값

    // 비밀번호를 입력하지 않았을 경우
    if (inputPw.trim().length === 0){

        pwMessage.innerText = "영어,숫자,특수문자(!,@,#,-,_) 6~20글자 사이로 입력해주세요.";

        pwMessage.classList.remove('confirm');

        pwMessage.classList.remove('error'); // confirm, error시 발생하는 요소들 제거(=검은색 글씨로 만듬)

        checkObj.memberPw = false;

        memberPw.value = ""; // 첫 띄어쓰기 입력을 못하도록 막음

        return; // 아래의 유효성 검사를 막하게 하고 끝냄
    }


    // 입력한 비밀번호가 유효한지 정규식 검사 진행

    const regExp = /^[a-zA-Z0-9!@#_-]{6,20}$/;

    
    // 입력받은 inputPw값이 유효하지 않은경우
    if(!regExp.test(inputPw)) {
        pwMessage.innerText = "비밀번호가 유효하지 않습니다.";

        pwMessage.classList.add("error");

        pwMessage.classList.remove("confirm");

        checkObj.memberPw = false;

        return;
    }


    // 유효한 inputPw를 입력한 경우

    pwMessage.innerText = "유효한 비밀번호 형식입니다.";

    pwMessage.classList.add("confirm");

    pwMessage.classList.remove("error");

    checkObj.memberPw = true;


    // 비밀번호 입력시에도 확인이랑 비교하는 함수 추가
    if(memberPwConfirm.value.length > 0 ){
    // 비밀번호 확인란에 무언가 입력이 되어있을 때
    checkPw();
    }

});


// 2. 비밀번호가 유효할 때 비밀번호 확인 유효성 검사를 수행
memberPwConfirm.addEventListener("input", () => {

    if(checkObj.memberPw){
        // memberPw가 유효하여 true인 경우
        checkPw();
        return;

        // 위에 작성했던 checkPw() 함수를 통해 유효성 검사를 실시
    }
    checkObj.memberPwConfirm = false;
    // memberPw가 유효하지 않게되면 위의 if문은 수행하지 않고 false
});




/* 닉네임 유효성 검사 */
const memberNickname = document.querySelector("#memberNickname");
const nickMessage = document.querySelector("#nickMessage");



memberNickname.addEventListener("input",e =>{
    const inputNickname = e.target.value;

    // 닉네임란에 입력이 안되어있을 때
    if(inputNickname.trim().length === 0){

        nickMessage.innerText = "한글,영어,숫자로만 2~10글자";

        nickMessage.classList.remove("confirm","error");

        checkObj.memberNickname = false;

        memberNickname.value = "";

        return;
    }


    // 닉네임 유효성검사를 위한 정규식

    const regExp = /^[가-힣a-zA-Z0-9]{2,10}$/;


    // 유효성 검사가 입력된 닉네임과 맞지 않을 때


    if(!regExp.test(inputNickname)){

        nickMessage.innerText = "유효하지 않는 닉네임 형식입니다.";

        nickMessage.classList.add("error");

        nickMessage.classList.remove("confirm");

        checkObj.memberNickname = false;

        return;

    }

    // 중복 검사
    fetch("/member/checkNickname?memberNickname=" + inputNickname)
    .then(response => response.text())
    .then(count =>{

        if(count == 1){

            nickMessage.innerText = "중복된 닉네임이 있습니다.";

            nickMessage.classList.add('error');

            nickMessage.classList.remove('confirm');

            checkObj.memberNickname = false;

            return;
        }

        if(count == 0){

        nickMessage.innerText = "사용 가능한 닉네임입니다.";

        nickMessage.classList.add('confirm');

        nickMessage.classList.remove('error');

        checkObj.memberNickname = true;

        }

    })


       

});




/* 전화번호 유효성 검사하기 */
const memberTel = document.querySelector("#memberTel");
const telMessage = document.querySelector("#telMessage");

memberTel.addEventListener("input", e =>{
    const inputTel = e.target.value;

    // 전화번호 아무것도 입력안함

    if(inputTel.trim().length === 0){
        telMessage.innerText = "전화번호를 입력해주세요.(- 제외)";
        telMessage.classList.remove('confirm','error');
        memberTel.value = "";
        checkObj.memberTel = false;
        return;
    }

    // 전화번호 정규식
    const regExp = /^01[0-9]{1}[0-9]{3,4}[0-9]{4}$/;


    // 정규식과 맞지않음
    if(!regExp.test(inputTel)){
        telMessage.innerText = "올바른 전화번호 형식이 아닙니다.";
        telMessage.classList.add('error');
        telMessage.classList.remove('confirm');
        checkObj.memberTel = false;
        return;
    }
        telMessage.innerText = "올바른 전화번호 형식입니다.";
        telMessage.classList.add('confirm');
        telMessage.classList.remove('error');
        checkObj.memberTel = true;
        
    
});




// ----------------------------------이메일 인증------------------------------------- //


const sendAuthKeyBtn = document.querySelector("#sendAuthKeyBtn"); // 인증번호 받기 버튼
const authKey = document.querySelector("#authKey"); // 인증번호 입력칸
const authKeyMessage = document.querySelector("#authKeyMessage"); // 인증번호 관련 메시지 출력 span
const checkAuthKeyBtn = document.querySelector("#checkAuthKeyBtn");


let authTimer; // 타이머 역할을 하는 setInterval을 저장할 변수


// 타이머 초기값은 5분(화면 상에는 타이머 시작시 4분 59초부터 시작)
const initMin = 4; 
const initSec = 59;
const initTime = "05:00";


// 실제 줄어드는 시간을 저장할 변수
let min = initMin;
let sec = initSec;


// 인증번호 받기 버튼 클릭
sendAuthKeyBtn.addEventListener("click", () => {

    checkObj.authKey = false;
    document.querySelector("#authKeyMessage").innerText = "";

    // 인증번호 받기 동작이 허용되는 조건 : 중복되지않고 유효한 이메일을 입력한 경우
    // = checkObj가 true이어야만함
    if(!checkObj.memberEmail){
        alert("유효한 이메일을 먼저 작성하세요.");
        return;
    }



    // 타이머 숫자 초기화
    min = initMin;
    sec = initSec;


    // 이전 동작중인 Interval 클리어
    clearInterval(authTimer);
    // 적어두지 않으면 버튼을 누를때마다 계속 함수가 호출되는게 쌓여서 타이머가 제대로 작동하지 않음


    // 버튼 클릭하면 다시 인증 유효성 검사 여부를 false로 설정
    checkObj.authKey = false; 
    

    // 비동기로 서버에서 메일 보내기
    // GET 방식으로 하면 주소창에 이메일을 입력함으로써 찾을 수 있는 악용 여지가 생기므로 POST 방식을 사용

    fetch("/email/signup",{
        method : "POST",
        headers : {"Content-Type" : "application/json"},
        body : memberEmail.value
    })
    .then(response => response.text())
    .then(result => {
        if(result == 1){
            console.log("인증번호 발송 성공");
        }else{
            console.log("인증번호 발송 실패");
        }
    })



    // 메일은 비동기로 서버에서 보내는 동안 화면에서는 타이머를 시작하도록 하기
    authKeyMessage.innerText = initTime; // 위에서 선언한 initTime이 텍스트로 표시(05:00)
    authKeyMessage.classList.remove("confirm","error");

    
    // setInterval(함수, 지연시간(ms)) : 지연시간(ms)만큼 시간이 지날 때 마다 함수를 수행함
        // 1초가 지날때마다 시간이 1초 줄어든 만큼의 함수 수행
    // clearInterval(Interval이 저장된 변수) : 매개변수로 전달받은 interval이 멈춤


    // 인증시간 출력하는 부분(1초마다 동작)
    authTimer = setInterval(() => {

        authKeyMessage.innerText = `${addZero(min)}:${addZero(sec)}`;

        // 0분 0초인 경우("00:00" 출력 후)
        if(min == 0 && sec == 0){
            checkObj.authKey = false; // 시간 안에 인증을 못하게됨

            clearInterval(authTimer); // 타이머를 종료시킴

            authKeyMessage.classList.add("error");

            authKeyMessage.classList.remove("confirm");

            return;
        }

        // sec가 0이 될 경우(0초를 화면에 출력한 다음)
        if(sec == 0){
            sec = 60;

            // 0초->59초 표기된 min -1 
            min--;

        }
        // 1초 감소
        sec--;

    }, 1000);


    
});

// 전달받은 숫자가 10 미만일 때 앞에 0을 붙여서 반환해줌(ex) 3을 03으로)
function addZero(number){
    
    if(number < 10 ) return "0"+ number;
    else return number;
}



// -----------------회원 가입 버튼 클릭시 전체 유효성 검사하기---------------------- //


const signUpForm = document.querySelector("#signUpForm");


// 회원 가입 폼이 제출됨
signUpForm.addEventListener("submit", e => {

    // checkObj에서 저장된 value중 하나라도 false가 있으면 제출을 막음
    // checkObj에 있는 요소를 하나하나 꺼내서 검사하므로 객체 전용 향상된 for문을 사용
    for(let key in checkObj){
        // checkObj 요소의 key 값을 순서대로 꺼냄

        if(!checkObj[key]){
            // checkObj의 key값이 false인게 발견될 경우
            let str; // 출력할 메시지를 저장하는 변수

            switch(key){
                case "memberEmail" : str = "이메일이 유효하지 않습니다."; break;
                case "authKey" : str = "이메일이 인증되지 않았습니다."; break;
                case "memberPw" : str = "비밀번호가 유효하지 않습니다."; break;
                case "memberPwConfirm" : str = "비밀번호 확인이 유효하지 않습니다."; break;
                case "memberNickname" : str = "닉네임이 유효하지 않습니다."; break;
                case "memberTel" : str = "전화번호가 유효하지 않습니다."; break;
            }
            alert(str);

            document.getElementById(key).focus(); // 유효하지 않는 곳에 포커스를 맞춰줌

            e.preventDefault();
            return; // form 제출 막고 종료
        }
    }
  

});




// -----------------------------  이메일 인증하기 -------------------



// 인증하기 버튼 클릭 후 비동기로 서버에 제출, 그 번호가 발급된 인증번호와 같은지 비교해서 같으면 1, 아니면 0 반환.
// 단, 타이머가 0:00가 아닐 경우에만 수행
checkAuthKeyBtn.addEventListener("click",() => {

    // 타이머가 0:00(0분 0초이면) 끝
    if (min === 0 && sec === 0){
        alert("인증번호 입력 제한시간이 지났습니다.");
        return;
    }
    // 6글자를 채우지 않고 인증하기 버튼을 눌렀을 때
    if(authKey.value.length < 6){
        alert("인증번호가 제대로 입력되지 않았습니다.");
        return;
    }
        
    // 입력받은 이메일과 인증번호를 저장하는 객체 생성
        const obj = {"email":memberEmail.value,"authKey" : authKey.value};
    
        fetch("/email/checkAuthKey", {
            method : "POST",
            headers : {"Content-Type" : "application/json"},
            body : JSON.stringify(obj)
        })
        .then(resp => resp.text())
        .then(result => {
            if (result === 0){ // == : 값만 비교 / === : 값+ 타입 둘다 비교
                // 전달받은 result값이 0인경우 인증 실패
                alert("인증번호가 일치하지 않습니다.");

                checkObj.authKey = false;

                return;
            }

                // 인증 성공
                clearInterval(authTimer); // 타이머를 멈춤

                alert("인증되었습니다.");

                authKeyMessage.innerText = "인증되었습니다.";

                authKeyMessage.classList.remove("error");

                authKeyMessage.classList.add("confirm");

                checkObj.authKey = true; // 유효성 검사 인증키 여부를 true로 변환
        })

    
});