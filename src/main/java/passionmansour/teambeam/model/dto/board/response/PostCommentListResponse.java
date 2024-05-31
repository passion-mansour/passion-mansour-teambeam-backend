package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.entity.PostComment;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentListResponse {
    private List<PostCommentResponse> postCommentResponseList = new ArrayList<>();

    public PostCommentListResponse form(List<PostCommentResponse> postCommentResponses){
        this.setPostCommentResponseList(postCommentResponses);
        return this;
    }

    public PostCommentListResponse entityToForm(List<PostComment> comments){
        List<PostCommentResponse> postCommentResponseList = new ArrayList<>();
        if(comments != null) {
            for (PostComment comment : comments) {
                postCommentResponseList.add(new PostCommentResponse().form(comment));
            }
        }
        this.setPostCommentResponseList(postCommentResponseList);

        return this;
    }
}
