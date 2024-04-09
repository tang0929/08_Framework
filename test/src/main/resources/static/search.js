const selectUserList = document.querySelector("#selectUserList");

const userList = document.querySelector("#userList");


selectUserList.addEventListener("click",() => {

    location.href ="/searchSuccess";
//    fetch("/user/selectUserList")
//    .then(response => response.json())
//    .then(userList =>{
//     console.log(userList)
//    }) 
});