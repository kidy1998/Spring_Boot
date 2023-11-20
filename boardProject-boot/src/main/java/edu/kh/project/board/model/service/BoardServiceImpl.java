package edu.kh.project.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dao.BoardMapper;
import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.Pagination;

@Service
public class BoardServiceImpl implements BoardService{

		@Autowired
		private BoardMapper mapper;

		
		// 게시판 종류 조회
		@Override
		public List<Map<String, Object>> selectBoardTypeList() {
			
			return mapper.selectBoardTypeList();
		}
		
		
		
		@Override
		public Map<String, Object> selectBoardList(int boardCode, int cp) {
		
			// 1.특정 게시판의 삭제되지 않은 게시글 수 조회
			int listCount = mapper.getListCount(boardCode);
			
			// 2. 게시글 수 조회 결과 + cp를 이용해 Pagination 객체 생성
			Pagination pagination = new Pagination(listCount, cp);
			
			// 3. 특정 게시판에서 현재 페이지에 해당하는 부분에 대한 게시글 목록 조회 
			// boardCode 에 따른 게시판에서 몇 페이지(Pagination.currnetPage) 에 대한 
			// 게시글 몇 개(pagination.limit)인지 조회
			
			 
			// RowBounds 객체 
			// - 마이바티스에서 페이징 처리를 위해 제공하는 객체
			// - offset 만큼 건너뛰고 
			// 그 다음 지정된 행 개수 만큼 조회

			// 1) offset 계산
			int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
			
			// 2) RowBounds 객체 생성
			RowBounds rowBounds = new RowBounds(offset,pagination.getLimit() );
				
			List<Board> boardList = mapper.selectBoardList(boardCode,rowBounds);
			
			Map<String,Object> map = new HashMap<String, Object>();
			
			map.put("pagination", pagination);
			map.put("boardList", boardList);
			
			// 리턴값이 2개라 map 으로 리턴
			return map;
		}


		/**
		 * 게시글 상세조회 
		 */
		@Override
		public Board selectBoard(Map<String, Object> map) {
			
			return mapper.selectBoard(map);
		}


		/**
		 * 좋아요 눌렀는지 확인
		 */
		@Override
		public int likeCheck(Map<String, Object> likeMap) {
			
			return  mapper.likeCheck(likeMap);
		}


		
		/**
		 * 조회수 증가 service
		 */
		@Transactional(rollbackFor = Exception.class)
		@Override
		public int updateReadCount(int boardNo) {
			
			return mapper.updateReadCount(boardNo);
		}

		@Transactional(rollbackFor = Exception.class)
		@Override
		public int like(Map<String, Integer> paramMap) {
			
			int result = 0;
			// check 값이 0이면 테이블에 INSERT, 1 이면 DELETE
			int check = paramMap.get("check");
			
			if(check == 0) {
				
				result =  mapper.likeInsert(paramMap); 
				
			}else {
				
				result = mapper.likeDelete(paramMap);
			}
			
			if(result == 0) return -1;  // 오류 발생 시(업데이트나 삭제 안됨)
			
			int count = mapper.countBoardLike(paramMap.get("boardNo"));
			
			
			return count;
			
		}


		/**
		 * 검색어 있을 경우 게시글 조회
		 */
		@Override
		public Map<String, Object> selectBoardList(Map<String, Object> paramMap, int cp) {
			
			// 1.특정 게시판의 삭제되지 않았고 검색 조건이 일치하는 게시글 수 조회
			int listCount = mapper.getSearchListCount(paramMap);
			
			// 2. 게시글 수 조회 결과 + cp를 이용해 Pagination 객체 생성
			Pagination pagination = new Pagination(listCount, cp);
			
			// 1) offset 계산
			int offset = (pagination.getCurrentPage() - 1) * pagination.getLimit();
			
			// 2) RowBounds 객체 생성
			RowBounds rowBounds = new RowBounds(offset,pagination.getLimit() );
			
			
			// 3. 특정 게시판에서 현재 페이지에 해당하는 부분에 대한 게시글 목록 조회 (검색어가 일치하는)
			
			List<Board> boardList = mapper.selectSearchBoardList(paramMap,rowBounds);
			
			Map<String,Object> map = new HashMap<String, Object>();
			
			map.put("pagination", pagination);
			map.put("boardList", boardList);
			
			// 리턴값이 2개라 map 으로 리턴
			
			return map;
		}

		

		/**
		 * db 이미지 파일 목록조회
		 */
		@Override
		public List<String> selectImageList() {
			
			return mapper.selectImageList();
		}
		
		
		
		
		
}
