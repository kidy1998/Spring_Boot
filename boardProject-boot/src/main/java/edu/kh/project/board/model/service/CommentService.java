package edu.kh.project.board.model.service;

import java.io.IOException;
import java.util.List;

import edu.kh.project.board.model.dto.Comment;

public interface CommentService {

	List<Comment> selectCommentList(int boardNo);

	int insertComment(Comment comment) throws Exception, IOException;

	int deleteComment(int commentNo);

	int updateComment(Comment comment);;

}
