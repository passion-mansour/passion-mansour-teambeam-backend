package passionmansour.teambeam.model.dto.tag.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Tag;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagListResponse {
    List<TagResponse> tagResponses = new ArrayList<>();

    public TagListResponse form(List<TagResponse> tagResponses){
        this.setTagResponses(tagResponses);
        return this;
    }

    public TagListResponse entityToForm(List<Tag> tags){
        if(tags != null) {
            for (Tag tag : tags) {
                this.tagResponses.add(new TagResponse().form(tag));
            }
        }

        return this;
    }
}
