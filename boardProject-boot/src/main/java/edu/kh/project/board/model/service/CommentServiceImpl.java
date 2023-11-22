package edu.kh.project.board.model.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dao.CommentMapper;
import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.exception.FileUploadException;

@Service
public class CommentServiceImpl implements CommentService{
	
	@Autowired
	CommentMapper dao;
	
	
	@Override
	public List<Comment> selectCommentList(int boardNo) {
		
		return dao.select(boardNo);
	}

	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int insertComment(Comment comment) throws Exception, IOException {
		
		int result = dao.insertComment(comment);
		
		if(result > 0) {
			
			return result;
		}else {
			
			throw new FileUploadException();
		}
		
		
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int deleteComment(int commentNo) {
		
		int result = dao.deleteComment(commentNo);
		
		if(result > 0) {
			
			return result;
		}else {
			
			throw new FileUploadException();
		}
	}


	@Override
	public int updateComment(Comment comment) {
		
		int result = dao.updateComment(comment);
		
		if(result > 0) {
			
			return result;
		}else {
			
			throw new FileUploadException();
		}
	}

}
