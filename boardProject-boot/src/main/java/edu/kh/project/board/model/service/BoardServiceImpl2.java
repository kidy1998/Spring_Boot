package edu.kh.project.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dao.BoardMapper2;
import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImage;
import edu.kh.project.board.model.exception.FileUploadException;
import edu.kh.project.board.model.exception.ImageDeleteException;
import edu.kh.project.common.utility.Util;


@Service
@PropertySource("classpath:/config.properties")
public class BoardServiceImpl2 implements BoardService2{
		
	
	@Value("${board.image.webpath}")
	private String webPath;
	
	@Value("${board.image.location}")
	private String filePath;
	
	
	@Autowired
	BoardMapper2 mapper;
	
	// 게시글 삽입
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int boardInsert(Board board, List<MultipartFile> images) throws Exception, IOException {
		
		int result = mapper.boardInsert(board);
		
		// insert 실패 시
		if(result == 0) return 0;
		
		// insert 성공 시 selectKey 태그로 인해 세팅된 값 얻어오기
		int boardNo = board.getBoardNo();
		
		
		//게시글 삽입 성공 시 업로드된 이미지가 있다면 Board_Img 테이블에 삽입
		if(boardNo > 0) { 
			
			// 실제 업로도된 파일의 정보를 기록할 list
			List<BoardImage> uploadList = new ArrayList<BoardImage>();
			
			
			for(int i=0; i<images.size(); i++ ) {	
				
				if(images.get(i).getSize() > 0) {
					
					BoardImage img = new BoardImage();
					
					// img 에 파일 정보 담아서 uploadList에 추가
					img.setImagePath(webPath); //웹 접근경로
					img.setBoardNo(boardNo);  //게시글 번호
					img.setImageOrder(i); // 이미지 순서
					
					String fileName = images.get(i).getOriginalFilename();
					
					img.setImageOriginal(fileName);  //원본명
					img.setImageReName(Util.fileRename(fileName)); // 변경명
					
					uploadList.add(img);
					
				}
					
			}
			
			
			if( !uploadList.isEmpty() ) {
				
				 result = mapper.insertImageList(uploadList);
				// result : 삽입된 행의 개수
				
				if(result == uploadList.size()) {
					
					// 	uploadList 크기만큼 전부 insert가 수행됐다면 => insert 성공
					for(int i=0; i<uploadList.size(); i++) {
						
						int index = uploadList.get(i).getImageOrder();
						
						String rename = uploadList.get(i).getImageReName();
						
						images.get(index).transferTo(new File(filePath + rename));
					}
					
					
					
				}else {  // 일부 혹은 전체 insert 실패 => rollback 필요
					// @Transactional : 예외 발생 시 rollback
					// rollback을 위해 강제로 예외를 발생시킴(사용자 정의 예외)
					
					throw new FileUploadException();
				}
				
				
			}
			
			
			
		}
		
		return boardNo;
	}
	
	// 게시글 수정 서비스
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int boardUpdate(Board board, List<MultipartFile> images,
			String deleteList) throws IllegalStateException, IOException {
		
		board.setBoardContent( Util.XSSHandling( board.getBoardContent()) );
		board.setBoardTitle(Util.XSSHandling( board.getBoardTitle()) );
		
		// 게시글만 업데이트하는 DAO 호출
		int rowCount = mapper.boardUpdate(board);
		
		// 게시글 부분이 수정에 성공했을 때
		if(rowCount > 0) {
			
			if(!deleteList.equals("")) { //삭제할 이미지가 있다면
				
				// 3.delteList 에 작성된 이미지 모두 삭제
				Map<String,Object> deleteMap = new HashMap<String,Object>();
				deleteMap.put("boardNo", board.getBoardNo());
				deleteMap.put("deleteList", deleteList);
				
				rowCount = mapper.imageDelete(deleteMap);
				
				if(rowCount == 0) { // 이미지 삭제 실패 시 예외 발생시킴 -> rollback
					
					throw new ImageDeleteException();
				}
			}
			
				//새로 업로드된 이미지 분류 작업
				
				// images : 실제 파일이 담긴 List
				//         -> input type="file" 개수만큼 요소가 존재
				//         -> 제출된 파일이 없어도 MultipartFile 객체가 존재
				
				List<BoardImage> uploadList = new ArrayList<>();
				
				for(int i=0 ; i<images.size(); i++) {
					
					if(images.get(i).getSize() > 0) { // 업로드된 파일이 있을 경우
						
						// BoardImage 객체를 만들어 값 세팅 후 
						// uploadList에 추가
						BoardImage img = new BoardImage();
						
						// img에 파일 정보를 담아서 uploadList에 추가
						img.setImagePath(webPath); // 웹 접근 경로
						img.setBoardNo(board.getBoardNo()); // 게시글 번호
						img.setImageOrder(i); // 이미지 순서
						
						// 파일 원본명
						String fileName = images.get(i).getOriginalFilename();
						
						img.setImageOriginal(fileName); // 원본명
						img.setImageReName( Util.fileRename(fileName) ); // 변경명    
						
						uploadList.add(img);
						
						// 오라클은 다중 UPDATE를 지원하지 않기 때문에
						// 하나씩 UPDATE 수행
						
						rowCount = mapper.imageUpdate(img);
						
						if(rowCount == 0) {
							// 수정 실패 == DB에 이미지가 없었다 
							// -> 이미지를 삽입
							rowCount = mapper.imageInsert(img);
						}
					}
				}
				
				
				// 5. uploadList에 있는 이미지들만 서버에 저장(transferTo())
				if(!uploadList.isEmpty()) {
					for(int i=0 ; i< uploadList.size(); i++) {
						
						int index = uploadList.get(i).getImageOrder();
						
						// 파일로 변환
						String rename = uploadList.get(i).getImageReName();
						
						images.get(index).transferTo( new File(filePath + rename)  );                    
					}
				}

			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		return 0;
	}
}
