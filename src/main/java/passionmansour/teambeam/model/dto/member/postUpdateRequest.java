package passionmansour.teambeam.model.dto.member;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.model.enums.PostType;

public class postUpdateRequest {
    @NotNull(message = "postId cannot be null")
    private Long postId;

    private String postTitle;
    @Lob
    private String postContent;
    private PostType postType;

    private Member member;
}
