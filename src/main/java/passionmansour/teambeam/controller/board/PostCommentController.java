package passionmansour.teambeam.controller.board;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.board.request.*;
import passionmansour.teambeam.model.dto.board.response.PostCommentResponse;
import passionmansour.teambeam.service.board.PostCommentService;

@Tag(name = "PostComment Controller", description = "게시물 댓글 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{projectId}/{boardId}/{postId}")
public class PostCommentController {
    @Autowired
    public final PostCommentService postCommentService;

    // 게시물 댓글 생성
    @PostMapping("/")
    public ResponseEntity<PostCommentResponse> createPost(@RequestHeader("Authorization") String token,
                                                          @PathVariable("postId") Long postId,
                                                          @Valid @RequestBody PostPostCommentRequest postPostCommentRequest){
        postPostCommentRequest.setPostId(postId);
        return ResponseEntity.ok(postCommentService.createPostComment(token, postPostCommentRequest));
    }

    // 게시물 댓글 업데이트
    @PatchMapping("/{commentId}")
    public ResponseEntity<PostCommentResponse> updatePost(@PathVariable("commentId") Long commentId,
                                                   @Valid @RequestBody PatchPostCommentRequest patchPostCommentRequest){
        patchPostCommentRequest.setPostCommentId(commentId);
        return ResponseEntity.ok(postCommentService.updatePostComment(patchPostCommentRequest));
    }
}
