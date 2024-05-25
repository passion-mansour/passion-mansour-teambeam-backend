package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.board.response.BookmarkListResponse;
import passionmansour.teambeam.model.dto.board.response.BookmarkResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.service.BookmarkService;

import java.util.List;

@Tag(name = "Bookmark Controller", description = "북마크 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my/bookmark")
public class BookmarkController {
    @Autowired
    public final BookmarkService bookmarkService;

    // 북마크 추가
    @PostMapping("/{postId}")
    public ResponseEntity<BookmarkResponse> saveBookmark(@RequestHeader("Authorization") String token,
                                                         @PathVariable("postId") Long postId){
        return ResponseEntity.ok(bookmarkService.saveBookmark(token, postId));
    }

    // TODO: 북마크 삭제

    // 북마크 상세조회
    @GetMapping("/{bookmarkId}")
    public ResponseEntity<PostResponse> getBookmarkById(@PathVariable("bookmarkId") Long bookmarkId){
        return ResponseEntity.ok(bookmarkService.sendToPost(bookmarkId));
    }

    // 태그들로 북마크들 조회
    @GetMapping("/tags")
    public ResponseEntity<BookmarkListResponse> getBookmarksByTags(@RequestHeader("Authorization") String token,
                                                                   @PathVariable("postId") List<Long> tags){
        return ResponseEntity.ok(bookmarkService.findAllByTag(token, tags));
    }

    // 유저 일련번호로 모든 북마크들 조회
    @GetMapping("/")
    public ResponseEntity<BookmarkListResponse> getBookmarksByToken(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(bookmarkService.findAllByToken(token));
    }
}
