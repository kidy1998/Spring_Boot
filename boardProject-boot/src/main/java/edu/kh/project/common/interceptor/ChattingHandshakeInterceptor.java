package edu.kh.project.common.interceptor;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpSession;

@Component
public class ChattingHandshakeInterceptor implements HandshakeInterceptor{

	// WebSocketHandler가 동작하기 전 
	// 웹소켓에 접속한 클라이언트의 세션(HttpSession)을 얻기 위해 조상인 ServerHttpRequest부터 차례로 
	// 다운캐스팅 + get 진행
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		
		if(request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request; //다운캐스팅
			
			HttpSession session = servletRequest.getServletRequest().getSession(); //세션 얻기
			
			//Map<String, Object> attributes : WebSocketHandler의 WebsocketSession 에 넣을 값을 세팅하는 Map
			attributes.put("session", session);
		}
		
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		// TODO Auto-generated method stub
		
	}

}
