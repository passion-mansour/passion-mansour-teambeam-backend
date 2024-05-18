package passionmansour.teambeam.model.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.enums.PostType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostPostDto {
    private String postTitle;
    private String postContent;
    private PostType postType;
}
