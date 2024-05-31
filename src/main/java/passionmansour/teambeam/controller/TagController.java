package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import passionmansour.teambeam.model.dto.tag.request.PostTagRequest;
import passionmansour.teambeam.model.dto.tag.response.TagListResponse;
import passionmansour.teambeam.model.dto.tag.response.TagResponse;
import passionmansour.teambeam.model.enums.TagCategory;
import passionmansour.teambeam.service.TagService;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Tag Controller", description = "태그 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/{projectId}")
public class TagController {
    @Autowired
    private final TagService tagService;

    // 태그 생성
    @PostMapping("/tag")
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody PostTagRequest postTagRequest,
                                                 @PathVariable("projectId") Long projectId){
        postTagRequest.setProjectId(projectId);
        return ResponseEntity.ok(tagService.createTags(postTagRequest));
    }

    // 태그 삭제
    @DeleteMapping("/tag/{tagId}")
    public ResponseEntity<?> deleteTag(@PathVariable("tagId") Long tagId){
        tagService.deleteTag(tagId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Delete tag successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 프로젝트 전체 태그 조회
    @GetMapping("/tag")
    public ResponseEntity<TagListResponse> getTagByProjectId(@PathVariable("projectId") Long projectId){
        return ResponseEntity.ok(tagService.getAllByProjectId(projectId));
    }

    // 프로젝트 post 전체 태그 조회
    @GetMapping("/post/tag")
    public ResponseEntity<TagListResponse> getTagByPostId(@PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(tagService.getAllByTagCategory(projectId, TagCategory.post));
    }

    // 프로젝트 schedule 전체 태그 조회
    @GetMapping("/schedule/tag")
    public ResponseEntity<TagListResponse> getTagByScheduleId(@PathVariable("projectId") Long projectId){
        return ResponseEntity.ok(tagService.getAllByTagCategory(projectId, TagCategory.schedule));
    }

    // 프로젝트 t0d0 전체 태그 조회
    @GetMapping("/todo/tag")
    public ResponseEntity<TagListResponse> getTagByTodoId(@PathVariable("projectId") Long projectId){
        return ResponseEntity.ok(tagService.getAllByTagCategory(projectId, TagCategory.todo));
    }
}
