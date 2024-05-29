package passionmansour.teambeam.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.request.PatchPostCommentRequest;
import passionmansour.teambeam.model.dto.board.request.PostPostCommentRequest;
import passionmansour.teambeam.model.dto.board.response.PostCommentListResponse;
import passionmansour.teambeam.model.dto.board.response.PostCommentResponse;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.entity.PostComment;
import passionmansour.teambeam.repository.*;
import passionmansour.teambeam.service.MemberService;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Post> post = postRepository.findById(postPostCommentRequest.getPostId());
        if (post.isEmpty()) {
            // TODO: 예외처리
        }

        PostComment postComment = PostComment.builder()
                .postCommentContent(postPostCommentRequest.getContent())
                .createDate(LocalDateTime.now())
                .member(jwtTokenService.getMemberByToken(token))
                .post(post.get())
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

    }

    @Transactional(readOnly = true)
    public PostComment getById(Long postCommentId) {
        Optional<PostComment> postComment = postCommentRepository.findById(postCommentId);
        if(postComment.isEmpty()){
            // TODO: 예외처리
        }

        return postComment.get();
    }

    @Transactional(readOnly = true)
    public PostCommentListResponse getAllByPostId(Long postId) {
        return new PostCommentListResponse().entityToForm(postCommentRepository.getAllByPostId(postId));
    }
}
