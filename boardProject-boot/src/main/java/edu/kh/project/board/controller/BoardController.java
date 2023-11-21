package edu.kh.project.board.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImage;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SessionAttributes({"loginMember"})
@RequestMapping("/board")
@Controller
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	
	
	
	// 게시글 목록 조회
		@GetMapping("/{boardCode:[0-9]+}") // boardCode는 1자리 이상 숫자
		public String selectBoardList( @PathVariable("boardCode") int boardCode,
									@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
									Model model, 
									@RequestParam Map<String, Object> paramMap // 파라미터 전부 다 담겨있음 key,query
									) {
			// Model : 데이터 전달 객체(기본 : request scope)
			
			if(paramMap.get("key") == null) { // 검색어가 없을 때 
				
				//boardCode 확인
				System.out.println("boardCode : " + boardCode);
				
				
				// 게시글 목록 조회 서비스 호출
				Map<String, Object> map = service.selectBoardList(boardCode, cp);
				
				// 조회 결과를 request scope에 세팅 후 forward
				model.addAttribute("map", map);
				
			}else {
				
				paramMap.put("boardCode", boardCode);
				
				Map<String, Object> map = service.selectBoardList(paramMap, cp);
				
				
				model.addAttribute("map", map);
				
			}
			
			
			
			
			return "board/boardList";
		}
		
		
		// 게시글 상세조회
		@GetMapping("/{boardCode}/{boardNo}")
		public String boardDetail(
				
				@PathVariable("boardCode") int boardCode,
				@PathVariable("boardNo") int boardNo,
				Model model,
				RedirectAttributes ra //리다이렉트 시 데이터 전달 객체
				//@SessionAttribute(value = "loginMember", required = false) Member loginMember
				// 매개변수에 @SessionAttribute 어노테이션 추가해서 사용해도 됨, 단 로그인이 필수가 아니기 때문에
				// required = false 추가해야됨!
				,HttpServletRequest req, HttpServletResponse resp
				) throws Exception {
			
			
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("boardCode", boardCode);
			map.put("boardNo", boardNo);
			
			
			Board board = service.selectBoard(map);
			
			String path = null;
			
			if(board != null) {
				path = "board/boardDetail";   //조회결과 있으면 상세조회결과로 foward
				model.addAttribute("board",board); // SQL 에서 조회한 값들 model 객체로 전달	
				
				//게시글의 이미지가 있을 경우 
				if(!board.getImageList().isEmpty()) {
					
					BoardImage thumbnail = null;
					
					if(board.getImageList().get(0).getImageOrder() == 0 ) { //썸네일이면
						thumbnail = board.getImageList().get(0);
					}
					
					model.addAttribute("thumbnail", thumbnail);
					
					// 썸네일 있으면 start = 1, 없으면 0
					model.addAttribute("start", thumbnail != null ? 1 :0 );
				}
				
				// 로그인 상태인 경우 회원번호 얻어와서 좋아요 여부 확인하는 service 호출
				// 좋아요를 눌렀으면 "likeCheck" 모델에 실어서 보냄
				Member loginMember = (Member) model.getAttribute("loginMember");
			
				
				 if(loginMember != null ) {
					 
					 int memberNo = loginMember.getMemberNo();
					 
					 Map<String,Object> likeMap = new HashMap<String, Object>();
					 likeMap.put("memberNo", memberNo);
					 likeMap.put("boardNo", boardNo);
					// 이렇게 안하고 위에 만든 map<boardCode,boardNo> 에 memberNo만 추가로 put 해도 됨!!
					 
					 
					 
					int likeCheck = service.likeCheck(likeMap);
					System.out.println("좋아요 조회결과 : " + likeCheck);
					
					if(likeCheck != 0) {
						
						model.addAttribute("likeCheck", likeCheck);
					}
					 
				 }
				 // ------------쿠키를 이용한 조회 수 증가 처리--------------------
				 
				 if(loginMember == null || loginMember.getMemberNo() != board.getMemberNo() ) {
					 
					 Cookie c = null;
					 
					 // 요청에 담겨있는 모든 쿠키 얻어오기
					 Cookie[] cookies = req.getCookies();	
					 
					 // 쿠키가 존재할 경우
					 if(cookies != null) {
						 
						 //쿠키 중 "readBoardNo" 라는 쿠키를 차장서 c 에 대입
						 for(Cookie cookie : cookies) {
							 if(cookie.getName().equals("readBoardNo")) {
								 c = cookie;
								 break;
							 }
							 
						 }
						 
					 }
					 // 기존 쿠키가 없거나 현재 게시글 번호가 쿠키에 저장되지 않은 경우(해당 게시글 본 적 없음)
					 
					 int result = 0;
					 
					 if(c == null) { // 쿠키 없음 -> 새로 생성
						 
						 // 게시글 번호 생성 -> "|" 구분자로 사용
						 c = new Cookie("readBoardNo","|" + boardNo + "|");
						 
						 result = service.updateReadCount(boardNo); // 조회수 증가 서비스
						 
					 }else {
						 
						 // 현재 게시글 번호가 쿠키게 있는지 확인
						 // Cookie.getValue(): 쿠키게 저장된 모든 값 얻어옴 -> String 으로 변환
						 // String.indexOf("문자열") : 찾는 문자열이 String 몇번 인덱스에 있는지 반환
						 // 없으면 -1 반환
						 
						 if(c.getValue().indexOf("|" + boardNo + "|") == -1 ) {
							 
							// 기존 값에 게시글 번호 추가해서 다시 세팅
							c.setValue(c.getValue() + "|" + boardNo + "|");
							result = service.updateReadCount(boardNo); // 조회수 증가 서비스
						 }
						 
						 
					 }
					 
					 if(result > 0) {
							board.setReadCount(board.getReadCount() + 1);
							// 조회된 board 조회 수와 DB 조회 수 동기화
							
							// 적용 경로 설정
							c.setPath("/"); //  "/" 이하 경로 요청 시 쿠키 서버로 전달
							
							// 수명 지정
							Calendar cal = Calendar.getInstance(); // 싱글톤 패턴
							cal.add(cal.DATE, 1);
							
							// 날짜 표기법 변경 객체 (DB의 TO_CHAR()와 비슷)
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							
							// java.util.Date
							Date a = new Date(); // 현재 시간
							
							Date temp = new Date(cal.getTimeInMillis()); // 내일 (24시간 후)
							// 2023-05-11 12:16:10
							
							Date b = sdf.parse(sdf.format(temp)); // 내일 0시 0분 0초
							
							
							// 내일 0시 0분 0초 - 현재 시간
							long diff = (b.getTime()  -  a.getTime()) / 1000; 
							// -> 내일 0시 0분 0초까지 남은 시간을 초단위로 반환
							
							c.setMaxAge((int)diff); // 수명 설정
							
							resp.addCookie(c); // 응답 객체를 이용해서
											   // 클라이언트에게 전달
						}
	
					 
				 	}
				 
				 
				 
				 //--------------------------------------------
				
					
					
				}else {
					
					path = "redirect:/board/" + boardCode; //없으면 게시판의 첫페이지로 리다이렉트
					ra.addFlashAttribute("message","해당 게시글이 존재하지 않습니다");
				}
				
				
				return path;
	
		}
		
		
		
		
		/** 좋아요
		 * @return
		 */
		@PostMapping("/like")
		@ResponseBody
		public int like(@RequestBody Map<String,Integer> paramMap) {
			// paramMap 에는 boardDetail.js에서 data 객체에 담아서 보낸 
			// boardNo,loginMemberNo,check 값이 담겨있음
			System.out.println("좋아요 눌렀는지 여부(0:안누름, 1:누름) :" + paramMap.get("check"));
			
			
			return service.like(paramMap);
		}
		
		
		
		
		
		
		
	
}
