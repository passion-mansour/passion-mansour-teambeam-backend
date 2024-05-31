package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Board;
import passionmansour.teambeam.model.entity.Post;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse {
    private List<PostResponse> postResponses = new ArrayList<>();;

    public PostListResponse form(List<PostResponse> postResponses){
        this.setPostResponses(postResponses);
        return this;
    }

    public PostListResponse entityToForm(List<Post> posts){
        if(posts != null) {
            for (Post post : posts) {
                this.postResponses.add(new PostResponse().form(post));
            }
        }

        return this;
    }
}
