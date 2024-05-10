

/* 웹소켓 테스트 */


// 1. SockJS 라이브러리 추가 -> common.html에 작성


// 2. SockJS 객체를 생성
const testSock = new SockJS("/testSock");

// - 객체 생성 시 자동으로 websocket://localhost(또는 ip)/testSock으로 연결 요청으로 보냄


// 3. 생성된 sockJS 객체를 이용해서 메시지를 전달
const sendMessageFn = (name, str) => {

    // JSON을 통해 다량의 데이터를 TEXT로 변경하여 전달
    const obj = {
        "name" : name,
        "str" : str
    };

    // 연결된 웹소켓 핸들러로 JSON을 전달
    testSock.send(JSON.stringify(obj));
}



// 4. 서버로부터 현재 클라이언트에게 웹소켓을 이용한 메시지가 전달된 경우
testSock.addEventListener("message",e => {

    // e.data : 서버로부터 전달받은 message
   // console.log(e.data);


   // JSON을 JS object로 변환
   const msg = JSON.parse(e.data);

   console.log(`${msg.name} : ${msg.str}`);
});