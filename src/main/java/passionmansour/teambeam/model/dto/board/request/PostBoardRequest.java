package passionmansour.teambeam.model.dto.board.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostBoardRequest {
    @NotNull
    private String name;
    @NotNull
    private Long projectId;
}
