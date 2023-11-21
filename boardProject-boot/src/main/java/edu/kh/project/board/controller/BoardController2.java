package edu.kh.project.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.service.BoardService2;
import edu.kh.project.member.model.dto.Member;


@SessionAttributes({"loginMember"})
@RequestMapping("/board2")
@Controller
public class BoardController2 {
	
	@Autowired
	private BoardService2 service;  // 삽입,수정,삭제
	
	@Autowired
	private BoardService boardService; // 목록, 상세조호
	
	
	@GetMapping("/{boardCode:[0-9]+}/insert")
	public String boardInsert( @PathVariable("boardCode") int boardCode) {
		
		//@PathVariable : 주소 값 가져오기 + request scope 에 값 올리기
	
		return "board/boardWrite";
		
	}
	
	@PostMapping("/{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode
			,Board board //필드에 파라미터 담김) 
			, @RequestParam(value="images",required = false) List<MultipartFile> images
			, @SessionAttribute("loginMember") Member loginMember
			, RedirectAttributes ra
			) throws IOException, Exception {
		
		// 파라미터 : 제목, 내용, 파일(0~5), 저장경로(HttpSession 에서 application scope로 얻음)
		// 세션(로그인한 회원정보), RedirectAttributes(리다이렉트 시 message 전달)
		
		/*
		 * List<MultipartFile>
		 * - 업로도된 이미지가 없어도 List에 MultipartFile 요소는 존재
		 *  단, 업로드된 이미지가 없는 요소에 경우 파일 크기(size)가 0, 
		 *  파일명(getOriginFileName())이 빈 칸 "" 으로 나옴
		 * 
		 */
		
		// 회원번호, boardCode 세팅
		board.setMemberNo(loginMember.getMemberNo());
		board.setBoardCode(boardCode);
		
		
		// 게시글 삽입 서비스 호출 호 삽입된 게시글 번호 반환받기
		int boardNo = service.boardInsert(board,images);
		
		String message = null;
		String path = "redirect:";
		
		
		
		if(boardNo > 0) {
			message = "게시글이 등록되었습니다";
			path += "/board/" + boardCode + "/" + boardNo;
			
		}
		
		ra.addFlashAttribute("message",message);
		
		
		return path;
		
	}
	
	
	// 게시글 수정 화면으로 전환
	@GetMapping("/{boardCode}/{boardNo}/update")
	public String boardUpdate(@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			Model model ) {
		
		// model 의 경우 request scope 범위이기 때문에 boardController 에서 설정한
		// board 객체는 이미 사라짐!! -> 다시 게시글 조회하는 메서드 실행해서 
		// 결과를 model 에 저장
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		Board board = boardService.selectBoard(map);
		model.addAttribute("board",board);
		
		return "board/boardUpdate";
	}
	
	
	@PostMapping("/{boardCode}/{boardNo}/update")
	public String boardUpdate( Board board,  // 커맨드객체(name == 필드명일 때 자동으로 세팅)
			@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, // 쿼리스트링(cp) 유지
			@RequestParam(value = "images",required = false) List<MultipartFile> images, // 업로드된 파일 리스트
			@RequestParam(value = "deleteList",required = false) String deleteList, // 삭제할 이미지 순서
			RedirectAttributes ra
			) throws IllegalStateException, IOException {
		
		board.setBoardCode(boardCode);
		board.setBoardNo(boardNo);
		// 커맨드 객체 board 에 얻어온 값 세팅
		
	
		
		int rowCount = service.boardUpdate(board,images,deleteList);
		
		String message = null;
		String path = "redirect:";
		
		if(rowCount >0) {
			message = "게시글이 수정되었습니다";
			path += "/board/" + boardCode + "/" + boardNo + "?cp=" + cp;
		}else {
			message = "게시글 수정 실패";
			path += "update";
		}
		
		ra.addFlashAttribute("message",message);
		
		return path;
	}
	
	
	
	
}
