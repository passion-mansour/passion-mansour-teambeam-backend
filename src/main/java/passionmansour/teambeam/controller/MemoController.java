package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.memo.request.PatchMemoRequest;
import passionmansour.teambeam.model.dto.memo.request.PostMemoRequest;
import passionmansour.teambeam.model.dto.memo.response.MemoListResponse;
import passionmansour.teambeam.model.dto.memo.response.MemoResponse;
import passionmansour.teambeam.service.MemoService;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Memo Controller", description = "메모 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my/memo")
public class MemoController {
    @Autowired
    public final MemoService memoService;

    // 메모 생성
    @PostMapping("/")
    public ResponseEntity<MemoResponse> createPost(@RequestHeader("Authorization") String token,
                                                   @Valid @RequestBody PostMemoRequest memoRequest){
        return ResponseEntity.ok(memoService.createMemo(token, memoRequest));
    }

    // 메모 업데이트
    @PatchMapping("/{memoId}")
    public ResponseEntity<MemoResponse> updatePost(@PathVariable("memoId") Long memoId,
                                                   @Valid @RequestBody PatchMemoRequest patchMemoRequest){
        patchMemoRequest.setMemoId(memoId);
        return ResponseEntity.ok(memoService.updateMemo(patchMemoRequest));
    }

    // 메모 삭제
    @DeleteMapping("/{memoId}")
    public ResponseEntity<?> deleteMemo(@PathVariable("memoId") Long memoId){
        memoService.deleteMemo(memoId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete memo successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 일련번호로 메모 조회
    @GetMapping("/{memoId}")
    public ResponseEntity<MemoResponse> getPostById(@PathVariable("memoId") Long memoId){
        return ResponseEntity.ok(new MemoResponse().form(memoService.getById(memoId)));
    }

    // 게시판 일련번호로 모든 메모 조회
    @GetMapping("/")
    public ResponseEntity<MemoListResponse> getPostsByBoardId(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok(memoService.getAllByToken(token));
    }
}
