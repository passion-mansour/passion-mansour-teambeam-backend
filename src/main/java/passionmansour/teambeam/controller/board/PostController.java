package passionmansour.teambeam.controller.board;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.board.request.PatchPostRequest;
import passionmansour.teambeam.model.dto.board.request.PostBoardRequest;
import passionmansour.teambeam.model.dto.board.request.PostPostRequest;
import passionmansour.teambeam.model.dto.board.response.PostListResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.service.BoardService;
import passionmansour.teambeam.service.PostService;

import java.util.List;

@Tag(name = "Post Controller", description = "게시물 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{projectId}/{boardId}")
public class PostController {
    @Autowired
    public final PostService postService;

    @PostMapping("/")
    public ResponseEntity<PostResponse> createPost(@RequestHeader("Authorization") String token,
                                                   @PathVariable("projectId") Long projectId,
                                                   @PathVariable("boardId") Long boardId,
                                                   @Valid @RequestBody PostPostRequest postPostRequest){
        postPostRequest.setProjectId(projectId);
        postPostRequest.setBoardId(boardId);
        return ResponseEntity.ok(postService.createPost(token, postPostRequest));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable("postId") Long postId,
                                                   @Valid @RequestBody PatchPostRequest patchPostRequest){
        patchPostRequest.setPostId(postId);
        return ResponseEntity.ok(postService.updatePost(patchPostRequest));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(new PostResponse().form(postService.getById(postId)));
    }

    @GetMapping("/title")
    public ResponseEntity<PostResponse> getPostByTitle(@PathVariable("title") String title){
        return ResponseEntity.ok(postService.getByTitle(title));
    }

    @GetMapping("/tags")
    public ResponseEntity<PostListResponse> getPostByTags(@PathVariable("postId") List<Long> tags){
        return ResponseEntity.ok(postService.getAllByTags(tags));
    }
}
