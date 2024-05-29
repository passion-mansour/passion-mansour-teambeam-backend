package passionmansour.teambeam.model.dto.Tag.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.model.entity.Tag;
import passionmansour.teambeam.model.enums.TagCategory;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {
    private Long tagId;
    private String tagName;
    private TagCategory tagCategory;
    private Long projectId;

    public TagResponse form(Tag tag) {
        return TagResponse.builder()
                .tagId(tag.getTagId())
                .tagName(tag.getTagName())
                .tagCategory(tag.getTagCategory())
                .projectId(tag.getProject().getProjectId())
                .build();
    }
}
