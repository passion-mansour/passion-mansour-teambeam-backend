package passionmansour.teambeam.controller.board;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.board.request.*;
import passionmansour.teambeam.model.dto.board.response.PostCommentListResponse;
import passionmansour.teambeam.model.dto.board.response.PostCommentResponse;
import passionmansour.teambeam.service.board.PostCommentService;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "PostComment Controller", description = "게시물 댓글 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{projectId}/{boardId}/{postId}")
public class PostCommentController {
    @Autowired
    public final PostCommentService postCommentService;

    // 게시물 댓글 생성
    @PostMapping("/")
    public ResponseEntity<PostCommentResponse> createPostComment(@RequestHeader("Authorization") String token,
                                                          @PathVariable("postId") Long postId,
                                                          @Valid @RequestBody PostPostCommentRequest postPostCommentRequest){
        postPostCommentRequest.setPostId(postId);
        return ResponseEntity.ok(postCommentService.createPostComment(token, postPostCommentRequest));
    }

    // 게시물 댓글 업데이트
    @PatchMapping("/{commentId}")
    public ResponseEntity<PostCommentResponse> updatePostComment(@PathVariable("commentId") Long commentId,
                                                   @Valid @RequestBody PatchPostCommentRequest patchPostCommentRequest){
        patchPostCommentRequest.setPostCommentId(commentId);
        return ResponseEntity.ok(postCommentService.updatePostComment(patchPostCommentRequest));
    }

    // 게시물 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteBoard(@PathVariable("commentId") Long commentId){
        postCommentService.deleteComment(commentId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete comment successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 해당 게시글의 전체 게시물 댓글 조회
    @GetMapping("/")
    public ResponseEntity<PostCommentListResponse> getPostCommentByPostId(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(postCommentService.getAllByPostId(postId));
    }
}
