package passionmansour.teambeam.model.dto.board.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import passionmansour.teambeam.model.enums.PostType;

import java.util.ArrayList;
import java.util.List;

@Data
public class PatchPostRequest {
    private Long postId;
    @NotNull
    private String title;
    private String content;
    @NotNull
    private PostType postType;
    private boolean notice;
    private List<Long> postTagIds = new ArrayList<>();
}
