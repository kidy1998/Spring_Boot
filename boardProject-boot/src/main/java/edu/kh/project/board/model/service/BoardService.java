package edu.kh.project.board.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.board.model.dto.Board;

public interface BoardService {

	// 게시판 종류 조회
	List<Map<String, Object>> selectBoardTypeList();

	/** 게시글 목록조회
	 * @param boardCode
	 * @param cp
	 * @return
	 */
	Map<String, Object> selectBoardList(int boardCode, int cp);


	Board selectBoard(Map<String, Object> map);


	int likeCheck(Map<String, Object> likeMap);


	int updateReadCount(int boardNo);


	int like(Map<String, Integer> paramMap);


	Map<String, Object> selectBoardList(Map<String, Object> paramMap, int cp);


	List<String> selectImageList();

}
