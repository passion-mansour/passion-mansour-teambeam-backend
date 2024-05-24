package passionmansour.teambeam.model.dto.board.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.entity.PostComment;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCommentResponse {
    private Long postCommentId;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updateDate;
    private Post post;

    public PostCommentResponse form(PostComment comment){
        return PostCommentResponse.builder()
                .postCommentId(comment.getPostCommentId())
                .content(comment.getPostCommentContent())
                .createDate(comment.getCreateDate())
                .updateDate(comment.getUpdateDate())
                .post(comment.getPost())
                .build();
    }
}
