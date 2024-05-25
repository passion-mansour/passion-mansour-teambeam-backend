package passionmansour.teambeam.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.request.PatchPostRequest;
import passionmansour.teambeam.model.dto.board.request.PostPostRequest;
import passionmansour.teambeam.model.dto.board.response.PostListResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.model.entity.Board;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.repository.BoardRepository;
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
    private final JwtTokenService jwtTokenService;
    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostResponse createPost(String token, PostPostRequest postPostRequest){
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
                .member(jwtTokenService.get(token))
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
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()){
            //TODO: 예외처리
        }

        return post.get();
    }

    @Transactional(readOnly = true)
    public PostResponse getByTitle(String postTitle){
        Optional<Post> post = postRepository.findByTitle(postTitle);
        if(post.isEmpty()){
            //TODO: 예외처리
        }

        return new PostResponse().form(post.get());
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllByTags(List<Long> tagIds){
        List<PostResponse> postResponses = new ArrayList<>();

        // 각각의 태그가 속한 모든 게시물 조회
        for(Long tagId : tagIds){
            for(Post post : postRepository.findAllByTag(tagId))
            postResponses.add(new PostResponse().form(post));
        }

        // 중복 제거
        List<PostResponse> distinctPostResponses = postResponses.stream()
                .distinct()
                .collect(Collectors.toList());

        return new PostListResponse(distinctPostResponses);
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllByBoardId(Long boardId){
        List<PostResponse> postResponses = new ArrayList<>();

        for (Post post : postRepository.findAllByBoardId(boardId)){
            postResponses.add(new PostResponse().form(post));
        }

        return new PostListResponse().form(postResponses);
    }
}
