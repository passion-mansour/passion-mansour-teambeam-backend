package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.board.response.BookmarkListResponse;
import passionmansour.teambeam.model.dto.board.response.BookmarkResponse;
import passionmansour.teambeam.model.dto.board.response.PostListResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.model.entity.Bookmark;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.service.BookmarkService;
import passionmansour.teambeam.service.board.PostService;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.awt.print.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Bookmark Controller", description = "북마크 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my/bookmark")
public class BookmarkController {
    @Autowired
    public final BookmarkService bookmarkService;
    @Autowired
    public final PostService postService;
    @Autowired
    public final JwtTokenService jwtTokenService;

    // 북마크 추가
    @PostMapping("/{postId}")
    public ResponseEntity<BookmarkResponse> saveBookmark(@RequestHeader("Authorization") String token,
                                                         @PathVariable("postId") Long postId){
        return ResponseEntity.ok(bookmarkService.saveBookmark(token, postId));
    }

    // 게시물 일련번호로 북마크 삭제
    @DeleteMapping("/post")
    public ResponseEntity<?> deleteBookmarkByPostId(@RequestHeader("Authorization") String token,
                                                    @RequestParam("postId") Long postId){

        Bookmark bookmark = bookmarkService.getByPostId(token, postId);
        bookmarkService.deleteBookmark(bookmark.getBookmarkId());
        if(bookmark != null){
            bookmarkService.checkDelete(bookmark);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete bookmark successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 북마크 일련번호로 북마크 삭제
    @DeleteMapping("/id")
    public ResponseEntity<?> deleteBookmarkByBookmarkId(@RequestParam("bookmarkId") Long bookmarkId){
        bookmarkService.deleteBookmark(bookmarkId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete bookmark successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 북마크 상세조회
    @GetMapping("/{bookmarkId}")
    public ResponseEntity<PostResponse> getBookmarkById(@PathVariable("bookmarkId") Long bookmarkId){
        return ResponseEntity.ok(bookmarkService.sendToPost(bookmarkId));
    }

    // 태그들로 북마크들 조회
    @GetMapping("/tags")
    public ResponseEntity<BookmarkListResponse> getBookmarksByTags(@RequestHeader("Authorization") String token,
                                                                   @RequestParam("tags") List<Long> tags){
        return ResponseEntity.ok(bookmarkService.getAllByTags(token, tags));
    }

    // 유저 일련번호로 모든 북마크들 조회
    @GetMapping("/")
    public ResponseEntity<BookmarkListResponse> getBookmarksByToken(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(bookmarkService.findAllByToken(token));
    }
}
