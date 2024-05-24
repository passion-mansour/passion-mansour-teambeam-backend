package passionmansour.teambeam.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Bookmark Controller", description = "북마크 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookmarkController {
}
