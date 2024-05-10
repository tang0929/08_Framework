package edu.kh.project.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import edu.kh.project.websocket.handler.NotificationWebsocketHandler;
import edu.kh.project.websocket.handler.TestWebsocketHandler;
import lombok.RequiredArgsConstructor;

@Configuration // 서버 실행 시 작성된 메서드를 모두 수행
@EnableWebSocket // 웹소켓 활성화 설정
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketConfigurer{

	
	// 웹소켓 처리 동작이 작성된 객체 의존성 주입(DI)
	private final TestWebsocketHandler testWebsocketHandler;
	
	
	// Bean으로 등록시켰던 SessionHandshakeInterceptor가 의존성 주입
	private final HandshakeInterceptor handshakeInterceptor;
	
	
	// 알림 웹소켓 처리 객체 의존성 주입
	private final NotificationWebsocketHandler notificationWebsocketHandler;
	
	
	// 웹소켓 핸들러를 등록하는 메서드
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		
		// addHandler(웹소켓 핸들러, 웹소켓 요청 주소)
		
		
		registry.addHandler(testWebsocketHandler, "/testSock")
		
		// websocket://loclhost/testSock으로 클라이언트가 요청하면 testWebsocketHandler가 처리하도록 등록
		
		
		.addInterceptors(handshakeInterceptor)
		// 클라이언트 연결 시 HttpSesstion을 가로채서 핸들러에게 전달
		
		.setAllowedOriginPatterns("http://localhost/","http://127.0.0.1/","http://192.168.10.14/")
		// 웹소켓 요청이 허용되는 ip/도메인 지정
		.withSockJS();
		// SockJS(websocket보완) 지원 + 브라우저 호완성 증가
		
		// 알림 
		registry
			.addHandler(notificationWebsocketHandler, "/notification/send")
			.addInterceptors(handshakeInterceptor)
			.setAllowedOriginPatterns("http://localhost/", 
							"http://127.0.0.1/", 
							"http://192.168.10.14/")
			.withSockJS();
		
		
		
		
	}
}
