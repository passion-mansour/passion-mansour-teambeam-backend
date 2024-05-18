package passionmansour.teambeam.model.dto.member;

import jakarta.validation.constraints.NotNull;

public class PostGetDto {
    @NotNull(message = "postId cannot be null")
    private Long postId;
}
