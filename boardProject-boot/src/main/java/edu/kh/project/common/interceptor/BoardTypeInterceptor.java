package edu.kh.project.common.interceptor;

import java.util.List;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import edu.kh.project.board.model.service.BoardService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Interceptor : 요청 혹은 응답을 가로채는 객체
// Client <-> Filter <-> Dispatcher Servlet <-> Interceptor <-> controller

// Interceptor : 요청에 대한 데이터 가공, 세부적으로 쓰일 보안,인증 관련 작업

// BoardTypeInterceptor의 기능 : 인터셉터가 실행되면 이러한 기능을 한다
public class BoardTypeInterceptor implements HandlerInterceptor{
	
	@Autowired
	private BoardService service;
	
	
	// preHandle(전처리) : Dispatcher Servlet -> Controller 사이
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// application scope 내장 객체 얻어오기
		ServletContext application = request.getServletContext();
		
		// application scope에  boardType이 조회되어 세팅되지 않았다면 -> 서버 시작 후 요청이 없을 경우
		if(application.getAttribute("boardTypeList") == null) {
			
			// 조회 서비스 호출
			System.out.println("Board_Type 조회 서비스 호출");
			
			List<Map<String,Object>> boardTypeList = service.selectBoardTypeList();
			
			System.out.println("boardTypeList : " + boardTypeList);
			
			// application scope 세팅
			
			application.setAttribute("boardTypeList", boardTypeList);
			
			
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
	// postHandle(후처리) : Controller -> Dispatcher Servlet 사이
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	// afterCompletion(view 완성 후) : 뷰 완성 후 View Resolver -> Dispatcher Servlet 사이
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
		
}
