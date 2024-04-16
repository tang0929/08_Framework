
/* 

    querySelector(), querySelectorAll()
    호출되었을 시점의 요소 형태를 그대로 얻어옴->실시간으로 추적 X

    getElementsByClassName()는 요소를 얻어와 계속 추적 -> 실시간으로 변경/변경된 값 확인 가능
*/


/* 업로드 예정인 이미지들 미리보기 */
const previewList = document.getElementsByClassName("preview");   // img태그 5개

const inputImageList = document.getElementsByClassName("inputImage");  // input 태그 5개

const deleteImageList = document.getElementsByClassName("delete-image");    // X 버튼 5개



// 이미지 선택 이후 취소를 누를 경우를 대비한 backup 이미지

/* 백업 원리 -> 복제품으로 기존 요소를 대체함 */
const backupInputList = new Array(inputImageList.length);



/* input 태그 값 변경 시 (파일 선택 시) 실행할 함수를 만듬 */
/**
 * 
 * @param inputImage : 파일이 선택된 input 태그 
 * @param order : 이미지 순서
 */
const changeImageFn = (inputImage, order) => {

    // 업로드 할 수 있는 크기를 10mb로 제한함 (10mb = 1024Kb * 1024B * 10)
    const maxSize = 1024 * 1024 * 10;


    // 업로드 된 파일 정보가 담긴 객체를 얻어와 file 변수에 저장
    const file = inputImage.files[0];




    if(file == undefined){
        // file선택 후 취소해서 없는 경우
        console.log("파일 선택 취소");
        

        // 같은 순서(order)에 위치한 backupInputList 요소를 얻어와 대체하기


        /* 한 번 화면에 추가된 요소는 재사용(다른 곳에 또 사용) 불가능 */


        // 백업본을 한 번 더 복제해서 temp에 저장
        const temp = backupInputList[order].cloneNode(true);


        inputImage.after(temp);  // 백업본을 다음 요소로 추가
        inputImage.remove();    // 원본을 삭제
        inputImage = temp;    // 원본 변수에 백업본을 참조할 수 있게 대입


        // 백업본에 없는 이벤트 리스너를 다시 추가
        inputImage.addEventListener("change", e => {
            changeImageFn(e.target, order);
        })


        return;
    }




    // 선택된 파일의 크기가 maxSize를 넘어섰을 경우

    if(file.size > maxSize){

        alert("10MB 이하의 이미지를 선택해주세요.");
        

        // 해당 순서의 백업요소가 없거나, 아무 파일도 선택된 적이 없는 경우
        if(backupInputList[order] == undefined || backupInputList[order].value == ''){
            
            inputImage.value = ""; // 잘못 업로드된 파일 값을 지움
        }


        // 이전에는 정상 선택을 해서 이미지가 올려져있는 상황에서 다른 이미지를 선택했는데 그 이미지가 용량초과인 경우
        // 백업본으로 다시 바꿔야함(재활용 코드)
        const temp = backupInputList[order].cloneNode(true);


        inputImage.after(temp);  // 백업본을 다음 요소로 추가
        inputImage.remove();    // 원본을 삭제
        inputImage = temp;    // 원본 변수에 백업본을 참조할 수 있게 대입


        // 백업본에 없는 이벤트 리스너를 다시 추가
        inputImage.addEventListener("change", e => {
            changeImageFn(e.target, order);
        })

        return;

    }



    // 정상적인 이미지가 선택된 경우 미리보기


    const reader = new FileReader(); // JS에서 파일을 읽고 저장하는 객체


    // 선택된 파일을 JS로 읽어오기
    reader.readAsDataURL(file);  // 데이터를 읽어서 URL 형식으로 변환 + reader.result 변수에 저장됨


    reader.addEventListener("load", e => {

        const url = e.target.result;


        // img 태그(.preview)에 src 속성으로 url 값을 대입

        previewList[order].src = url;


        // 같은 순서 backupInputList에 input 태그를 복제해서 대입
        backupInputList[order] = inputImage.cloneNode(true);
    });



}




// 이미지들을 하나하나 비교함
for(let i = 0 ; i < inputImageList.length ; i++){
    // input태그에 이미지가 선택된 경우(값이 변경된 경우)
    inputImageList[i].addEventListener("change",e => {

        // changeImageFn이라는 함수를 호출한다.
        changeImageFn(e.target,i);
        return;

    })



    // X 버튼 클릭시 업로드 예정이던 이미지 삭제
    deleteImageList[i].addEventListener("click", () => {

        // img, input, backup의 인덱스가 모두 일치한다는 특징을 이용


        previewList[i].src = ""; // 미리보기 이미지 삭제
        inputImageList[i].value = ""; // input에 선택된 이미지 삭제
        backupInputList[i].value = ""; // 백업용 이미지 삭제
    });
}




/* 제목, 내용이 입력이 안되어있을 경우 제출 못하게 하는 유효성 검사 */


document.querySelector("#boardWriteFrm").addEventListener("submit", e => {

    const boardTitle = document.querySelector("[name='boardTitle']");
    const boardContent = document.querySelector("[name='boardContent']");


    // 제목 미작성시
    if(boardTitle.value.trim().length == 0){
        alert("제목을 입력하세요.");

        // 제목쪽으로 커서를 옮김
        boardTitle.focus();

        // form 제출 막음
        e.preventDefault();
        
        return;
    }


    // 내용 미작성시
    if(boardContent.value.trim().length == 0){
        alert("내용을 입력하세요.");

        // 제목쪽으로 커서를 옮김
        boardContent.focus();

        // form 제출 막음
        e.preventDefault();

        return;
    }
})







/* 삭제 버튼 클릭시
1. 삭제하시겠습니까? 확인/취소
2. 취소하면 취소되었습니다. 끝
3. 확인하면 /editBoard/{boardCode}/{boardNo}/delete GET 방식요청 
4. {boardCode} 게시판의 {boardNo} 글의 BOARD_DEL_FL 값을 Y로 변경 
5. 변경 성공시 해당 게시판 목록 1page로 리다이렉트, 실패시 보고있던 페이지 그대로.*/