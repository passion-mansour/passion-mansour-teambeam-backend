package passionmansour.teambeam.model.dto.board.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostPostCommentRequest {
    @NotNull
    private String content;
    private Long postId;
}
