package passionmansour.teambeam.model.dto.board.response;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Bookmark;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Post;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponse {
    private Long bookmarkId;
    private Member member;
    private Post post;

    public BookmarkResponse form(Bookmark bookmark){
        return BookmarkResponse.builder()
                .bookmarkId(bookmark.getBookmarkId())
                .member(bookmark.getMember())
                .post(bookmark.getPost())
                .build();
    }
}
