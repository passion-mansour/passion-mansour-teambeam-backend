package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Bookmark;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkListResponse {
    List<BookmarkResponse> bookmarkResponses = new ArrayList<>();

    public BookmarkListResponse form(List<BookmarkResponse> bookmarkResponses){
        return BookmarkListResponse.builder()
                .bookmarkResponses(bookmarkResponses)
                .build();
    }
}
