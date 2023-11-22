package edu.kh.project.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.board.model.dto.Comment;

@Mapper
public interface CommentMapper {

	
	
	public List<Comment> select(int boardNo);

	
	public int insertComment(Comment comment);

	public int deleteComment(int commentNo);


	public int updateComment(Comment comment);

	
}
