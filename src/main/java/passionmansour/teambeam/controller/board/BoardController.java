package passionmansour.teambeam.controller.board;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.board.request.PostBoardRequest;
import passionmansour.teambeam.model.dto.board.response.BoardListResponse;
import passionmansour.teambeam.model.dto.board.response.BoardResponse;
import passionmansour.teambeam.service.board.BoardService;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Board Controller", description = "게시판 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{projectId}")
public class BoardController {
    @Autowired
    public final BoardService boardService;

    // 게시판 생성
    @PostMapping("/board")
    public ResponseEntity<BoardResponse> createBoard(@PathVariable("projectId") Long projectId,
                                                     @Valid @RequestBody PostBoardRequest postBoardRequest){
        postBoardRequest.setProjectId(projectId);
        return ResponseEntity.ok(boardService.createBoard(postBoardRequest));
    }

    // 게시판 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("boardId") Long boardId){
        boardService.deleteBoard(boardId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete board successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로젝트 일련번호로 모든 게시판 조회
    @GetMapping("/board")
    public ResponseEntity<BoardListResponse> getAllBoardByProjectId(@PathVariable("projectId") Long projectId){
        return ResponseEntity.ok(boardService.getAllBoardByProjectId(projectId));
    }
}
