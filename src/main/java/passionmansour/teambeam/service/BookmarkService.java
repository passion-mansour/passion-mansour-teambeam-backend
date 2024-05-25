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
    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;

    @Transactional
    public BookmarkResponse saveBookmark(String token, Long postId){
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()){
            // TODO: 예외처리
        }

        Bookmark bookmark = Bookmark.builder()
                .member(jwtTokenService.getMemberByToken(token))
                .post(post.get())
                .build();

        return new BookmarkResponse().form(bookmarkRepository.save(bookmark));
    }

    @Transactional
    public void deleteBookmark(String token, Long postId){
        // TODO: 기능 구현
    }

    @Transactional(readOnly = true)
    public Bookmark findById(Long bookmarkId){
        Optional<Bookmark> bookmark = bookmarkRepository.findById(bookmarkId);
        if(bookmark.isEmpty()){
            // TODO: 예외 처리
        }

        return bookmark.get();
    }

    public PostResponse sendToPost(Long bookmarkId){
        Optional<Bookmark> bookmark = bookmarkRepository.findById(bookmarkId);
        if(bookmark.isEmpty()){
            // TODO: 예외 처리
        }

        return new PostResponse().form(bookmark.get().getPost());
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
    public BookmarkListResponse findAllByTag(String token, List<Long> tagIds){
        List<BookmarkResponse> bookmarkResponses = new ArrayList<>();

        // 각각의 태그가 속한 모든 북마크 조회
        for(Long tagId : tagIds){
            for(Bookmark bookmark : bookmarkRepository.findAllByTag(jwtTokenService.getMemberByToken(token).getMemberId(), tagId))
                bookmarkResponses.add(new BookmarkResponse().form(bookmark));
        }

        // 중복 제거
        List<BookmarkResponse> distinctBookmarkResponses = bookmarkResponses.stream()
                .distinct()
                .collect(Collectors.toList());

        return new BookmarkListResponse().form(distinctBookmarkResponses);
    }
}
