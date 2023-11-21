package edu.kh.project.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImage;

@Mapper
public interface BoardMapper2 {
	
	
	/** 게시글 삽입
	 * @param board
	 * @return boardNo
	 */
	public int boardInsert(Board board);

	
	/** 이미지 리스트 삽입
	 * @param uploadList
	 * @return
	 */
	public int insertImageList(List<BoardImage> uploadList);



	public int boardUpdate(Board board);

	// 이미지 삭제
	public int imageDelete(Map<String, Object> deleteMap);



	// 이미지 수정
	public int imageUpdate(BoardImage img);


	// 이미지 삽입
	public int imageInsert(BoardImage img);
	
	
}
