package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.request.PatchPostCommentRequest;
import passionmansour.teambeam.model.dto.board.request.PostPostCommentRequest;
import passionmansour.teambeam.model.dto.board.response.PostCommentResponse;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.entity.PostComment;
import passionmansour.teambeam.repository.*;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostCommentService {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenService tokenService;

    @Transactional
    public PostCommentResponse createPostComment(String token, PostPostCommentRequest postPostCommentRequest) {
        Member member = getMemberByToken(token);

        Optional<Post> post = postRepository.findById(postPostCommentRequest.getPostId());
        if (post.isEmpty()) {
            // TODO: 예외처리
        }

        PostComment postComment = PostComment.builder()
                .postCommentContent(postPostCommentRequest.getContent())
                .createDate(LocalDateTime.now())
                .member(member)
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

    @Transactional(readOnly = true)
    public PostComment getById(Long postId) {
        return postCommentRepository.findById(postId).get();
    }

    @Transactional(readOnly = true)
    public List<PostCommentResponse> getAllByPostId(Long postId) {
        return postCommentRepository.getAllByPostId(postId);
    }

    @Transactional(readOnly = true)
    private Member getMemberByToken(String token) {
        // 토큰에서 회원 이름 확인
        String usernameFromToken = tokenService.getUsernameFromToken(token);

        // 해당 회원 정보 조회
        return memberRepository.findByMemberName(usernameFromToken)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with memberName: " + usernameFromToken));
    }
}
