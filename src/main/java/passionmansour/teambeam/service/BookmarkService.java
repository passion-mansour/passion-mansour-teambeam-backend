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
import java.util.Objects;

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
        Member member = jwtTokenService.getMemberByToken(token);
        Bookmark existingBookmark = bookmarkRepository.findByMemberAndPostAndIsDeleted(member.getMemberId(), post.getPostId());

        if (existingBookmark != null) {
            // 이미 존재하는 북마크가 있으면 삭제 플래그를 다시 설정하고 저장
            existingBookmark.set_deleted(false);

            bookmarkRepository.save(existingBookmark);
            log.info("existingBookmark status is " + existingBookmark.is_deleted());
            return new BookmarkResponse().form(existingBookmark);
        } else {
            // 중복된 북마크가 없으면 새로운 북마크 생성
            Bookmark newBookmark = Bookmark.builder()
                    .member(member)
                    .post(post)
                    .build();

            log.info("새 북마크 생성");
            return new BookmarkResponse().form(bookmarkRepository.save(newBookmark));
        }
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId){
        Bookmark bookmark = getById(bookmarkId);
        bookmarkRepository.delete(bookmark);
    }

    @Transactional
    public Bookmark getById(Long bookmarkId){
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new RuntimeException("Bookmark not found"));

        if(bookmark.getPost() != null){
            return bookmark;
        }

        throw new NullPointerException("Post is deleted");
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
            if(bookmark.getPost() != null){
                BookmarkResponse bookmarkResponse = new BookmarkResponse().form(bookmark);
                bookmarkResponse.getPost().setBookmark(true);
                bookmarkResponses.add(bookmarkResponse);
            }
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
