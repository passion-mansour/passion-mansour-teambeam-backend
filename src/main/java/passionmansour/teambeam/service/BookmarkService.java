package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.response.BookmarkListResponse;
import passionmansour.teambeam.model.dto.board.response.BookmarkResponse;
import passionmansour.teambeam.model.dto.board.response.PostListResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.model.entity.Bookmark;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.repository.BookmarkRepository;
import passionmansour.teambeam.repository.PostRepository;
import passionmansour.teambeam.service.board.PostService;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {
    private final JwtTokenService jwtTokenService;
    private final PostService postService;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public BookmarkResponse saveBookmark(String token, Long postId){
        Post post = postService.getById(postId);

        Bookmark bookmark = Bookmark.builder()
                .member(jwtTokenService.getMemberByToken(token))
                .post(post)
                .build();

        return new BookmarkResponse().form(bookmarkRepository.save(bookmark));
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId){
        Bookmark bookmark = getById(bookmarkId);
        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public Bookmark getById(Long bookmarkId){
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        return bookmark;
    }

    public PostResponse sendToPost(Long bookmarkId){
        Bookmark bookmark = getById(bookmarkId);

        return new PostResponse().form(bookmark.getPost());
    }

    @Transactional(readOnly = true)
    public PostListResponse findAllByToken(String token){
        Member member = jwtTokenService.getMemberByToken(token);
        List<PostResponse> postResponses = new ArrayList<>();

        for(Bookmark bookmark : member.getBookmarks()){
            postResponses.add(new PostResponse().form(bookmark.getPost()));
        }

        return new PostListResponse().form(postResponses);
    }

    @Transactional(readOnly = true)
    public PostListResponse getAllByTags(String token, List<Long> tagIds){
        Member member = jwtTokenService.getMemberByToken(token);

        List<PostResponse> postResponses = new ArrayList<>();

        for(Bookmark bookmark : bookmarkRepository.findAllByTagIds(member.getMemberId(), tagIds, tagIds.size())){
            postResponses.add(new PostResponse().form(bookmark.getPost()));
        }

        return new PostListResponse().form(postResponses);
    }
}
