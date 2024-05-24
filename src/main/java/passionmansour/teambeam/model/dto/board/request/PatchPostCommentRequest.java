package passionmansour.teambeam.model.dto.board.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatchPostCommentRequest {
    @NotNull
    private Long postCommentId;
    @NotNull
    private String content;
    @NotNull
    private Long postId;
}
