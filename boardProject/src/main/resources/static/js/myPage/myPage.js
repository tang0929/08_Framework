
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





// -------------------------------------------------------

/* 프로필 이미지 추가/변경/삭제 */

// 프로필 이미지 페이지 form 태그
const profile = document.querySelector("#profile"); 


// 프로필 이미지가 새로 업로드 되거나 삭제 되었음을 기록하는 
// 상태 변수

// -1 : 초기 상태(변화 없음)
//  0 : 프로필 이미지 삭제
//  1 : 새 이미지 선택
let statusCheck = -1;


// input type="file" 태그가의 값이 변경 되었을 때
// 변경된 상태를 백업해서 저장할 변수
// -> 파일이 선택/취소된 input을 복제해서 저장

// 요소. cloneNode(true|false) : 요소 복제(true 작성 시 하위 요소도 복제)
let backupInput;




if(profile != null){

  // img 태그 (프로필 이미지가 보여지는 요소)
  const profileImg = document.querySelector("#profileImg"); 

  // input type="file" 태그 (실제 업로드할 프로필 이미지를 선택하는 요소)
  let imageInput = document.querySelector("#imageInput"); 

  // x버튼 (프로필 이미지를 제거하고 기본 이미지로 변경하는 요소)
  const deleteImage = document.querySelector("#deleteImage"); 





  /* input type="file"의 값이 변했을 때 동작할 함수(이벤트 핸들러) */
  const changeImageFn = e => {

    // 업로드 가능한 파일 최대 크기 지정하여 필터링

    const maxSize =  1024 * 1024 * 5;
    // 5MB == 1024KB * 5 == 1024B * 1024 * 5

    console.log("e.target", e.target); // input

    console.log("e.target.value", e.target.value); // 변경된 값(파일명)

    // 선택된 파일에 대한 정보가 담긴 배열 반환
    // -> 왜 배열?? multiple 옵션에 대한 대비
    console.log("e.target.files", e.target.files); 

    // 업로드된 파일이 1개 있으면 files[0]에 저장됨

    // 업로드된 파일이 없으면 files[0] == undefined
    console.log("e.target.files[0]", e.target.files[0]); 

    const file = e.target.files[0];


    // ------------업로드된 파일이 없다면(취소한 경우)------------
    if(file == undefined){
      console.log("파일 선택 후 취소됨");

      
      // 파일 선택 후 취소 -> value == ''
      // -> 선택한 파일 없음으로 기록됨
      // -> backupInput으로 교체 시켜서
      //    이전 이미지가 남아 있는 것 처럼 보이게 함

      // 백업의 백업본
      const temp = backupInput.cloneNode(true);

      // input 요소 다음에 백업 요소 추가
      imageInput.after(backupInput);

      // 화면에 존재하는 기존 input 제거
      imageInput.remove();

      // imageInput 변수에 백업을 대입해서 대신하도록 함
      imageInput = backupInput;

      // 화면에 추가된  백업본에는 
      // 이벤트 리스너가 존재하지 않기 때문에 추가
      imageInput.addEventListener("change", changeImageFn);

      // 한번 화면에 추가된 요소(backupInput)는 재사용 불가능
      //  backupInput의 백업본이 temp를 backupInput 으로 변경
      backupInput = temp;

      return;
    }
    

    // ----------- 선택된 파일이 최대 크기를 초과한 경우 ------------
    if(file.size > maxSize){
      alert("5MB 이하의 이미지 파일을 선택해 주세요.");

      // 선택한 이미지가 없는데 5MB 초과하는 이미지를 선택한 경우
      if(statusCheck == -1){
        imageInput.value = '';
     
      } else{ // 기존 선택한 이미지가 있는데
              // 다음 선택한 이미지가 최대 크기를 초과한 경우

        // 백업의 백업본
        const temp = backupInput.cloneNode(true);

        // input 요소 다음에 백업 요소 추가
        imageInput.after(backupInput);

        // 화면에 존재하는 기존 input 제거
        imageInput.remove();

        // imageInput 변수에 백업을 대입해서 대신하도록 함
        imageInput = backupInput;

        // 화면에 추가된  백업본에는 
        // 이벤트 리스너가 존재하지 않기 때문에 추가
        imageInput.addEventListener("change", changeImageFn);

        // 한번 화면에 추가된 요소(backupInput)는 재사용 불가능
        //  backupInput의 백업본이 temp를 backupInput 으로 변경
        backupInput = temp;
      }


      return;
    }


    // ------------- 선택된 이미지 미리보기 ----------------

    // JS에서 파일을 읽을 때 사용하는 객체
    // - 파일을 읽고 클라이언트 컴퓨터에 저장할 수 있음
    const reader = new FileReader();

    // 선택한 파일(file) 을 읽어와
    // BASE64 인코딩 형태로 읽어와 result 변수에 저장
    reader.readAsDataURL(file); // -> 읽어오기 이벤트(load)

    // 읽어오기 끝났을 때
    reader.addEventListener("load", e => {

      // e.target == reader

      // 읽어온 이미지 파일이 BASE64 형태로 반환됨
      const url = e.target.result; // reader.result

      // 프로필 이미지(img)에 src속성으로 url값 세팅
      profileImg.setAttribute("src", url);
      
      // 새 이미지 선택 상태를 기록
      statusCheck = 1;

      // 파일이 선택된 input을 복제해서 백업
      backupInput = imageInput.cloneNode(true);

    });

  }



  // change 이벤트 : 새로운 값이 기존 값과 다를 경우 발생 
  imageInput.addEventListener("change", changeImageFn);


  // ------------ x버튼 클릭 시 기본 이미지로 변경 ----------------
  deleteImage.addEventListener("click", () => {

    // 프로필 이미지(img)를 기본 이미지로 변경
    profileImg.src = "/images/user.png";

    // input에 저장된 값(value)를 ''(빈칸)으로 변경
    //   -> input에 저장된 파일 정보가 모두 사라짐 == 데이터 삭제
    imageInput.value = '';

    backupInput = undefined; // 백업본도 삭제

    // 삭제 상태임을 기록
    statusCheck = 0;
  });


  // ------------ #profile (form) 제출 시 -----------------
  profile.addEventListener("submit", e => {
    
    let flag = true;


    // 기존 프로필 이미지가 없다가 새 이미지가 선택된 경우
    if(loginMemberProfileImg == null && statusCheck == 1) flag = false;

    // 기존 프로필 이미지가 있다가 삭제한 경우
    if(loginMemberProfileImg != null && statusCheck == 0) flag = false;
    
    // 기존 프로필 이미지가 있다가 새 이미지가 선택된 경우
    if(loginMemberProfileImg != null && statusCheck == 1) flag = false;


    if(flag){ // flag 값이 true인 경우
      e.preventDefault();
      alert("이미지 변경 후 클릭하세요")
    }
  });


}





