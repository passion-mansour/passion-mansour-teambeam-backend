package passionmansour.teambeam.model.dto.board.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.dto.tag.response.TagResponse;
import passionmansour.teambeam.model.dto.member.response.CreatorInfoResponse;
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
    private boolean notice;
    private boolean isBookmark;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private CreatorInfoResponse member;
    private Long projectId;
    private Long boardId;
    private String boardName;
    private List<TagResponse> postTags = new ArrayList<>();

    public PostResponse form(Post post){
        List<TagResponse> postTags = new ArrayList<>();

        for(PostTag postTag : post.getPostTags()){
            postTags.add(new TagResponse().form(postTag.getTag()));
        }

        return PostResponse.builder()
                .postId(post.getPostId())
                .title(post.getPostTitle())
                .content(post.getPostContent())
                .postType(post.getPostType())
                .notice(post.isNotice())
                .createDate(post.getCreateDate())
                .updateDate(post.getUpdateDate())
                .member(new CreatorInfoResponse().form(post.getMember()))
                .projectId(post.getProject().getProjectId())
                .boardId(post.getBoard().getBoardId())
                .boardName(post.getBoard().getBoardName())
                .postTags(postTags)
                .build();
    }
}
