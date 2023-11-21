package edu.kh.project.board.model.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;

public interface BoardService2  {

	int boardInsert(Board board, List<MultipartFile> images ) throws Exception, IOException;

	int boardUpdate(Board board, List<MultipartFile> images, String deleteList) throws IllegalStateException, IOException;

}
