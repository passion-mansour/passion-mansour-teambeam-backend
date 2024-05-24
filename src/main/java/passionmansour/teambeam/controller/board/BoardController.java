package passionmansour.teambeam.controller.board;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.board.request.PostBoardRequest;
import passionmansour.teambeam.model.dto.board.response.BoardListResponse;
import passionmansour.teambeam.model.dto.board.response.BoardResponse;
import passionmansour.teambeam.service.BoardService;

@Tag(name = "Board Controller", description = "게시판 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{projectId}")
public class BoardController {
    @Autowired
    public final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<BoardResponse> createBoard(@PathVariable("projectId") Long projectId,
                                                     @Valid @RequestBody PostBoardRequest postBoardRequest){
        postBoardRequest.setProjectId(projectId);
        return ResponseEntity.ok(boardService.createBoard(postBoardRequest));
    }

    @GetMapping("/board")
    public ResponseEntity<BoardListResponse> getAllBoardByProjectId(@PathVariable("projectId") Long projectId){
        return ResponseEntity.ok(boardService.getAllBoardByProjectId(projectId));
    }
}
