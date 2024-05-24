package passionmansour.teambeam.model.dto.board.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import passionmansour.teambeam.model.enums.PostType;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostPostRequest {
    @NotNull
    private String title;
    private String content;
    @NotNull
    private PostType postType;
    @NotNull
    private Long memberId;
    @NotNull
    private Long projectId;
    @NotNull
    private Long boardId;
    private List<Long> postTagIds = new ArrayList<>();
}
