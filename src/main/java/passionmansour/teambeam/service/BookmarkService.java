package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.response.BookmarkListResponse;
import passionmansour.teambeam.model.dto.board.response.BookmarkResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.model.entity.Bookmark;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.repository.BookmarkRepository;
import passionmansour.teambeam.service.board.PostService;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.util.ArrayList;
import java.util.List;

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
    public BookmarkListResponse findAllByToken(String token){
        Member member = jwtTokenService.getMemberByToken(token);
        List<BookmarkResponse> bookmarkResponses = new ArrayList<>();

        for(Bookmark bookmark : member.getBookmarks()){
            bookmarkResponses.add(new BookmarkResponse().form(bookmark));
        }

        return new BookmarkListResponse().form(bookmarkResponses);
    }

    @Transactional(readOnly = true)
    public BookmarkListResponse getAllByTags(String token, List<Long> tagIds){
        Member member = jwtTokenService.getMemberByToken(token);
        List<BookmarkResponse> bookmarkResponses = new ArrayList<>();

        for(Bookmark bookmark : bookmarkRepository.findAllByTagIds(member.getMemberId(), tagIds)){
            bookmarkResponses.add(new BookmarkResponse().form(bookmark));
        }

        return new BookmarkListResponse().form(bookmarkResponses);
    }
}
