package passionmansour.teambeam.model.dto.board.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import passionmansour.teambeam.model.enums.PostType;

import java.util.ArrayList;
import java.util.List;

@Data
public class PatchPostRequest {
    @NotNull
    private Long postId;
    @NotNull
    private String title;
    private String content;
    @NotNull
    private PostType postType;
    @NotNull
    private Long memberId;
    private List<Long> postTagIds = new ArrayList<>();
}
