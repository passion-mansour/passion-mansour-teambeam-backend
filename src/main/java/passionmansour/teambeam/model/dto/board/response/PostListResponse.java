package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Post;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse {
    List<PostResponse> postResponses = new ArrayList<>();

    public PostListResponse form(List<PostResponse> postResponses){
        return PostListResponse.builder()
                .postResponses(postResponses)
                .build();
    }
}
