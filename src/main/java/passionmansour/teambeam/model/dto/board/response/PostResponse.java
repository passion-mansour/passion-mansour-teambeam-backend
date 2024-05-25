package passionmansour.teambeam.model.dto.board.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.model.enums.PostType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private PostType postType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updateDate;
    private Long memberId;
    private String memberName;
    private Long projectId;
    private Long boardId;
    private String boardName;
    private List<PostTag> postTags = new ArrayList<>();;
    private List<PostCommentResponse> postComments = new ArrayList<>();;

    public PostResponse form(Post post){
        return PostResponse.builder()
                .postId(post.getPostId())
                .title(post.getPostTitle())
                .content(post.getPostContent())
                .postType(post.getPostType())
                .createDate(post.getCreateDate())
                .updateDate(post.getUpdateDate())
                .memberId(post.getMember().getMemberId())
                .memberName(post.getMember().getMemberName())
                .projectId(post.getProject().getProjectId())
                .boardId(post.getBoard().getBoardId())
                .boardName(post.getBoard().getBoardName())
                .postTags(post.getPostTags())
                .postComments(new PostCommentListResponse().entityToForm(post.getPostComments()).getPostCommentResponseList())
                .build();
    }
}
