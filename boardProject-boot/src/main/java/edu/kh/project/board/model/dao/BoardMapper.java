package edu.kh.project.board.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import edu.kh.project.board.model.dto.Board;

@Mapper
public interface BoardMapper {

	//게시판 종류 조회
	List<Map<String, Object>> selectBoardTypeList();
	
	
	/** boardCode 에 해당하는 게시판에 삭제되지 않은 게시글 조회
	 * @param boardCode
	 * @return listCount
	 */
	public int getListCount(int boardCode);


	
	/** 특정 게시판에서 현재 페이지에 해당하는 부분에 대한 게시글 목록 조회
	 * @param pagination
	 * @param boardCode
	 * @return
	 */
	public List<Board> selectBoardList(int boardCode, RowBounds rowBounds);



	public Board selectBoard(Map<String, Object> map);



	public int likeCheck(Map<String, Object> likeMap);
	

	/** 조회수 증가
	 * @param boardNo
	 * @return
	 */
	public int updateReadCount(int boardNo);



	/** 좋아요 
	 * @param paramMap
	 * @return
	 */
	public int likeInsert(Map<String, Integer> paramMap);



	/** 좋아요 삭제
	 * @param paramMap
	 * @return
	 */
	public int likeDelete(Map<String, Integer> paramMap);



	/** 좋아요 개수조회
	 * @param integer
	 * @return
	 */
	public int countBoardLike(Integer boardNo);



	/**게시글 수 조회
	 * @param paramMap
	 * @return
	 */
	public int getSearchListCount(Map<String, Object> paramMap);


	/** 게시글 목록 조회(검색)
	 * @param pagination
	 * @param paramMap
	 * @return
	 */
	public List<Board> selectSearchBoardList( Map<String, Object> paramMap, RowBounds rowBounds);



	/** dB 이미지 파일 목록 조회
	 * @return
	 */
	public List<String> selectImageList();
	
	
}
