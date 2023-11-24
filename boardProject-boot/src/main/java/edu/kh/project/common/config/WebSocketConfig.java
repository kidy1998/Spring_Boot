package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import edu.kh.project.chatting.model.websocket.ChattingWebsocketHandler;

@Configuration
@EnableWebSocket  // 여기서 웹소켓 사용할 것임을 명시
public class WebSocketConfig implements WebSocketConfigurer{

	@Autowired
	private ChattingWebsocketHandler chattingWebsocketHandler;
	
	@Autowired
	private HandshakeInterceptor handshakeInterceptor;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		registry.addHandler(chattingWebsocketHandler, "/chattingSock")
			.addInterceptors(handshakeInterceptor)
			.setAllowedOriginPatterns("http://localhost/", "http://127.0.0.1")
			//CORS 에러를 방지하기 위해 origin 패턴을 제한(내 컴퓨터에서 접속했을 때만 채팅 가능)
			.withSockJS();
	}

}
