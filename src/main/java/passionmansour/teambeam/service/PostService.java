package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.request.PatchPostRequest;
import passionmansour.teambeam.model.dto.board.request.PostPostRequest;
import passionmansour.teambeam.model.dto.board.response.PostListResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.model.entity.Board;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.repository.BoardRepository;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.repository.PostRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenService tokenService;

    @Transactional
    public PostResponse createPost(String token, PostPostRequest postPostRequest){
        Member member = getMemberByToken(token);

        Optional<Project> project = projectRepository.findById(postPostRequest.getProjectId());
        if(project.isEmpty()){
            //TODO: 예외처리
        }
        Optional<Board> board = boardRepository.findById(postPostRequest.getBoardId());
        if(board.isEmpty()){
            //TODO: 예외처리
        }

        Post post = Post.builder()
                .postTitle(postPostRequest.getTitle())
                .postContent(postPostRequest.getContent())
                .postType(postPostRequest.getPostType())
                .createDate(LocalDateTime.now())
                .member(member)
                .project(project.get())
                .board(board.get())
                // TODO: tag 서비스 필요
//                .postTags(postPostRequest.getPostTagIds().stream()
//                        .map()
//                        .toList())
                .build();

        return new PostResponse().form(postRepository.save(post));
    }

    @Transactional
    public PostResponse updatePost(PatchPostRequest patchPostRequest){
        // TODO: 생성자인 경우에만 수정 가능한지 여부 확인 필요
        Post post = getById(patchPostRequest.getPostId());

        post.setPostTitle(patchPostRequest.getTitle());
        post.setPostContent(patchPostRequest.getContent());
        post.setPostType(patchPostRequest.getPostType());
        post.setUpdateDate(LocalDateTime.now());
        // TODO: tag 서비스 필요
//        post.setPostTags();

        return new PostResponse().form(postRepository.save(post));
    }

    @Transactional(readOnly = true)
    public Post getById(Long postId){
        return postRepository.findById(postId).get();
    }

    @Transactional(readOnly = true)
    public PostResponse getByTitle(String postTitle){
        return postRepository.findByTitle(postTitle).get();
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllByTags(List<Long> tagIds){
        List<PostResponse> postResponses = new ArrayList<>();

        for(Long tagId : tagIds){
            for(PostResponse postResponse : postRepository.findAllByTag(tagId))
            postResponses.add(postResponse);
        }

        List<PostResponse> distinctPostResponses = postResponses.stream()
                .distinct()
                .collect(Collectors.toList());

        return new PostListResponse(distinctPostResponses);
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllByBoardId(Long boardId){
        return postRepository.findAllByBoardId(boardId);
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
