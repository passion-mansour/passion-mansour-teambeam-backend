package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.dto.member.response.CreatorInfoResponse;
import passionmansour.teambeam.model.entity.Bookmark;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponse {
    private Long bookmarkId;
    private CreatorInfoResponse member;
    private PostResponse post;

    public BookmarkResponse form(Bookmark bookmark){
        return BookmarkResponse.builder()
                .bookmarkId(bookmark.getBookmarkId())
                .member(new CreatorInfoResponse().form(bookmark.getMember()))
                .post(new PostResponse().form(bookmark.getPost()))
                .build();
    }
}
