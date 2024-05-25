package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Bookmark;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponse {
    private Long bookmarkId;
    private Long memberId;
    private String memberName;
    private Long postId;
    private String postTitle;

    public BookmarkResponse form(Bookmark bookmark){
        return BookmarkResponse.builder()
                .bookmarkId(bookmark.getBookmarkId())
                .memberId(bookmark.getMember().getMemberId())
                .memberName(bookmark.getMember().getMemberName())
                .postId(bookmark.getPost().getPostId())
                .postTitle(bookmark.getPost().getPostTitle())
                .build();
    }
}
