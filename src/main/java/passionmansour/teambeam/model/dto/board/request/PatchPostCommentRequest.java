package passionmansour.teambeam.model.dto.board.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatchPostCommentRequest {
    private Long postCommentId;
    @NotNull
    private String content;
    private Long postId;
}
