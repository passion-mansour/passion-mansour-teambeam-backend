package passionmansour.teambeam.model.dto.member;

import jakarta.validation.constraints.NotNull;

public class PostGetRequest {
    @NotNull(message = "postId cannot be null")
    private Long postId;
}
