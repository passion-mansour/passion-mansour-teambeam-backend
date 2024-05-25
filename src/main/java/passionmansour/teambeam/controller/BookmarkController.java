package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.board.response.BookmarkListResponse;
import passionmansour.teambeam.model.dto.board.response.BookmarkResponse;
import passionmansour.teambeam.service.BookmarkService;

import java.util.List;

@Tag(name = "Bookmark Controller", description = "북마크 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookmarkController {
    @Autowired
    public final BookmarkService bookmarkService;

    // 북마크 추가
    @PostMapping("/")
    public ResponseEntity<BookmarkResponse> saveBookmark(@RequestHeader("Authorization") String token,
                                                         @PathVariable("postId") Long postId){
        return ResponseEntity.ok(bookmarkService.saveBookmark(token, postId));
    }

    // TODO: 북마크 삭제


    // 태그들로 북마크들 조회
    @GetMapping("")
    public ResponseEntity<BookmarkListResponse> getBookmarksByTags(@RequestHeader("Authorization") String token,
                                                                   @PathVariable("postId") List<Long> tags){
        return ResponseEntity.ok(bookmarkService.findAllByTag(token, tags));
    }

    // 유저 일련번호로 모든 북마크들 조회
    @GetMapping("")
    public ResponseEntity<BookmarkListResponse> getBookmarksByToken(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(bookmarkService.findAllByToken(token));
    }
}
