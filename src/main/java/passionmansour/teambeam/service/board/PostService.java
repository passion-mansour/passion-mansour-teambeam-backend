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
import passionmansour.teambeam.repository.BookmarkRepository;
import passionmansour.teambeam.repository.PostRepository;
import passionmansour.teambeam.repository.ProjectRepository;
import passionmansour.teambeam.service.BookmarkService;
import passionmansour.teambeam.service.TagService;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public PostResponse createPost(String token, PostPostRequest postPostRequest) {
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
        for (Long postTagId : postPostRequest.getPostTagIds()) {
            postTags.add(tagService.addPostTag(postTagId, save.getPostId()));
        }
        save.setPostTags(postTags);

        return new PostResponse().form(save);
    }

    @Transactional
    public PostResponse updatePost(PatchPostRequest patchPostRequest) {
        Post post = getById(patchPostRequest.getPostId());

        post.setPostTitle(patchPostRequest.getTitle());
        post.setPostContent(patchPostRequest.getContent());
        post.setPostType(patchPostRequest.getPostType());
        post.setNotice(patchPostRequest.isNotice());
        post.setUpdateDate(LocalDateTime.now());

        // postTag 업데이트
        List<PostTag> postTags = new ArrayList<>();

        Set<Long> existingPostTagIds = post.getPostTags().stream()
                .map(postTag -> postTag.getTag().getTagId())
                .collect(Collectors.toSet());

        // !주의 : request로 넘어오는 id는 tagId기 때문에 postTagId가 아닌 tagId로 비교해야 한다.
        Set<Long> newPostTagIds = new HashSet<>(patchPostRequest.getPostTagIds());

        // 새로운 태그 추가
        for (Long postTagId : newPostTagIds) {
            if (!existingPostTagIds.contains(postTagId)) {
                PostTag newPostTag = tagService.addPostTag(postTagId, patchPostRequest.getPostId());
                postTags.add(newPostTag);
                log.info("PostTag 저장: " + postTagId);
            }
        }

        // 기존 태그 삭제
        for (PostTag postTag : post.getPostTags()) {
            if (!newPostTagIds.contains(postTag.getTag().getTagId())) {
                tagService.deletePostTag(postTag);
                log.info("PostTag 삭제: " + postTag.getPostTagId());
            } else postTags.add(postTag);
        }
        post.setPostTags(postTags);

        return new PostResponse().form(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = getById(postId);
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Post getById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return post;
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllByTags(List<Long> tagIds) {
        return new PostListResponse().entityToForm(postRepository.findAllByTagIds(tagIds));
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllByBoardId(String token, Long boardId) {
        List<PostResponse> postResponses = new ArrayList<>();

        for (Post post : postRepository.findAllByBoardId(boardId)) {
            PostResponse postResponse = new PostResponse().form(post);
            postResponse.setBookmark(isBookmark(token, post.getPostId()));
            postResponses.add(postResponse);
        }

        return new PostListResponse().form(postResponses);
    }

    @Transactional(readOnly = true)
    public boolean isBookmark(String token, Long postId) {
        Post post = getById(postId);

        Member member = jwtTokenService.getMemberByToken(token);

        for (Bookmark bookmark : member.getBookmarks()) {
            if(bookmark != null) {
                if (post.equals(bookmark.getPost())) return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public PostListResponse getNoticePost(Long projectId) {
        Project projectOptional = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return new PostListResponse().entityToForm(postRepository.findAllByNoticeIsTrueAndProject(projectOptional));
    }
}