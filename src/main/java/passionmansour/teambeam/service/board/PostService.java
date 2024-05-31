package passionmansour.teambeam.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.request.PatchPostRequest;
import passionmansour.teambeam.model.dto.board.request.PostPostRequest;
import passionmansour.teambeam.model.dto.board.response.PostListResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.repository.BoardRepository;
import passionmansour.teambeam.repository.PostRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.service.TagService;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {
    private final JwtTokenService jwtTokenService;
    private final TagService tagService;
    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostResponse createPost(String token, PostPostRequest postPostRequest){
        Project project = projectRepository.findById(postPostRequest.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Board board = boardRepository.findById(postPostRequest.getBoardId())
                .orElseThrow(() -> new RuntimeException("Board not found"));

        Post post = Post.builder()
                .postTitle(postPostRequest.getTitle())
                .postContent(postPostRequest.getContent())
                .postType(postPostRequest.getPostType())
                .notice(postPostRequest.isNotice())
                .createDate(LocalDateTime.now())
                .member(jwtTokenService.getMemberByToken(token))
                .project(project)
                .board(board)
                .build();

        Post save = postRepository.save(post);

        // tag 저장
        List<PostTag> postTags = new ArrayList<>();
        for (Long postTagId : postPostRequest.getPostTagIds()){
            postTags.add(tagService.addPostTag(postTagId, save.getPostId()));
        }
        save.setPostTags(postTags);

        return new PostResponse().form(save);
    }

    @Transactional
    public PostResponse updatePost(PatchPostRequest patchPostRequest){
        // TODO: 생성자인 경우에만 수정 가능한지 여부 확인 필요
        Post post = getById(patchPostRequest.getPostId());

        post.setPostTitle(patchPostRequest.getTitle());
        post.setPostContent(patchPostRequest.getContent());
        post.setPostType(patchPostRequest.getPostType());
        post.setUpdateDate(LocalDateTime.now());

        // tag 저장
        List<PostTag> postTags = new ArrayList<>();
        for (Long tagId : patchPostRequest.getPostTagIds()){
            postTags.add(tagService.addPostTag(tagId, patchPostRequest.getPostId()));
        }
        post.setPostTags(postTags);

        return new PostResponse().form(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long postId){
        Post post = getById(postId);
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Post getById(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return post;
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllByTags(List<Long> tagIds){
        return new PostListResponse().entityToForm(postRepository.findAllByTagIds(tagIds, tagIds.size()));
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllByBoardId(String token, Long boardId){
        List<PostResponse> postResponses = new ArrayList<>();
        PostResponse postResponse = new PostResponse();

        for(Post post : postRepository.findAllByBoardId(boardId)){
            postResponse.form(post);
            postResponse.setBookmark(isBookmark(token, post.getPostId()));
            postResponses.add(postResponse);
        }

        return new PostListResponse().form(postResponses);
    }

    public boolean isBookmark(String token, Long postId){
        Post post = getById(postId);
        Member member = jwtTokenService.getMemberByToken(token);

        for(Bookmark bookmark : member.getBookmarks()){
            if(post.equals(bookmark.getPost())) return true;
        }
        return false;
    }

    @Transactional
    public PostListResponse getNoticePost(Long projectId){
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        return new PostListResponse().entityToForm(postRepository.findAllByNoticeIsTrueAndProject(projectOptional.get()));

    }
}
