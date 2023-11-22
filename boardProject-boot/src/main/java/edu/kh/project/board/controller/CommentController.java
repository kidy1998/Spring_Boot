package edu.kh.project.board.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.service.CommentService;


// @Controller + @ResponseBody
@RestController // 요청/응답 처리(모든 요청, 응답은 비동기)
public class CommentController {
	@Autowired
	CommentService service;
	
		
	//댓글 목록 조회					// json 통신 시 한글 깨짐 방지
	@GetMapping(value="/comment", produces="application/json; charset=UTF-8")
	public List<Comment> select(int boardNo) { 
		List<Comment> list =  service.selectCommentList(boardNo);	

		return list;
	}
	
	
	//댓글 삽입
	@PostMapping("/insert")
	public int insertComment(@RequestBody Comment comment) throws IOException, Exception {
		
		
		int result = service.insertComment(comment);
		System.out.println("댓글 삽입결과 : " + result);
		return result;
	}
	
	
	
	//댓글 삭제
	@GetMapping(value="/delete", produces="application/json; charset=UTF-8")
	public int deleteComment(int commentNo) {
		
		return service.deleteComment(commentNo);
	}
	
	
	
	
	//댓글 수정
	@PostMapping("/update")
	public int updateComment(@RequestBody Comment comment) throws IOException, Exception {
			
		System.out.println("comment : " + comment);
		int result = service.updateComment(comment);
		System.out.println("댓글 수정결과 : " + result);
		return result;
			
	}
	
	
	@PostMapping("/reply")
	public int replyComment(@RequestBody Comment comment) throws IOException, Exception {
		
		System.out.println("comment : " + comment);
		int result = service.insertComment(comment);
		System.out.println("답글 입력결과 : " + result);
		return result;
	
	}
	
	
	
	
}
