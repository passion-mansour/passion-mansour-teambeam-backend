package passionmansour.teambeam.model.dto.Tag.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import passionmansour.teambeam.model.enums.TagCategory;

@Data
public class PostTagRequest {
    private String tagName;
    private TagCategory tagCategory;
    private Long projectId;

    @JsonCreator // deserialize 대상 json과 target entity가 정확하게 매칭되지 않을때 유용
    public PostTagRequest fromString(@JsonProperty("tagName") String tagName,
                                  @JsonProperty("tagCategory") String tagCategory) {
        this.tagName = tagName;
        this.tagCategory = TagCategory.fromString(tagCategory);
        return this;
    }
}
