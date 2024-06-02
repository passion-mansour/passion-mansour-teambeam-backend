package passionmansour.teambeam.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.request.PatchPostCommentRequest;
import passionmansour.teambeam.model.dto.board.request.PostPostCommentRequest;
import passionmansour.teambeam.model.dto.board.response.PostCommentListResponse;
import passionmansour.teambeam.model.dto.board.response.PostCommentResponse;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.entity.PostComment;
import passionmansour.teambeam.repository.*;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostCommentService {
    private final JwtTokenService jwtTokenService;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    @Transactional
    public PostCommentResponse createPostComment(String token, PostPostCommentRequest postPostCommentRequest) {
        Post post = postRepository.findById(postPostCommentRequest.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        PostComment postComment = PostComment.builder()
                .postCommentContent(postPostCommentRequest.getContent())
                .createDate(LocalDateTime.now())
                .member(jwtTokenService.getMemberByToken(token))
                .post(post)
                .build();

        return new PostCommentResponse().form(postCommentRepository.save(postComment));
    }

    @Transactional
    public PostCommentResponse updatePostComment(PatchPostCommentRequest patchPostCommentRequest) {
        // TODO: 생성자인 경우에만 수정 가능여부를 서버에서 진행하는 지 여부 확인

        PostComment postComment = getById(patchPostCommentRequest.getPostCommentId());

        postComment.setPostCommentContent(patchPostCommentRequest.getContent());
        postComment.setUpdateDate(LocalDateTime.now());

        return new PostCommentResponse().form(postCommentRepository.save(postComment));
    }

    @Transactional
    public void deleteComment(Long postCommentId){
        PostComment postComment = getById(postCommentId);

        postCommentRepository.delete(postComment);
    }

    @Transactional(readOnly = true)
    public PostComment getById(Long postCommentId) {
        PostComment postComment = postCommentRepository.findById(postCommentId)
                .orElseThrow(() -> new RuntimeException("PostComment not found"));

        return postComment;
    }

    @Transactional(readOnly = true)
    public PostCommentListResponse getAllByPostId(Long postId) {
        return new PostCommentListResponse().entityToForm(postCommentRepository.getAllByPostId(postId));
    }
}
