

// 조회 버튼을 클릭하고 표 조회하기

const selectBookList = document.querySelector("#selectBookList");


const bookList = document.querySelector("#bookList");


selectBookList.addEventListener("click",() => {

    fetch("/book/selectBookList")
    .then(response => response.json())
    .then(memberList => {
        console.log(bookList);
    })

});




