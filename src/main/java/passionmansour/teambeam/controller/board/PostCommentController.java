package passionmansour.teambeam.controller.board;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.board.request.*;
import passionmansour.teambeam.model.dto.board.response.PostCommentResponse;
import passionmansour.teambeam.model.dto.board.response.PostListResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.service.BoardService;
import passionmansour.teambeam.service.PostCommentService;
import passionmansour.teambeam.service.PostService;

import java.util.List;

@Tag(name = "PostComment Controller", description = "게시물 댓글 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{projectId}/{boardId}/{postId}")
public class PostCommentController {
    @Autowired
    public final PostCommentService postCommentService;

    @PostMapping("/")
    public ResponseEntity<PostCommentResponse> createPost(@RequestHeader("Authorization") String token,
                                                          @PathVariable("postId") Long postId,
                                                          @Valid @RequestBody PostPostCommentRequest postPostCommentRequest){
        postPostCommentRequest.setPostId(postId);
        return ResponseEntity.ok(postCommentService.createPostComment(token, postPostCommentRequest));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<PostCommentResponse> updatePost(@PathVariable("commentId") Long commentId,
                                                   @Valid @RequestBody PatchPostCommentRequest patchPostCommentRequest){
        return ResponseEntity.ok(postCommentService.updatePostComment(patchPostCommentRequest));
    }
}
