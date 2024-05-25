package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkListResponse {
    private List<BookmarkResponse> bookmarkResponses = new ArrayList<>();;

    public BookmarkListResponse form(List<BookmarkResponse> bookmarkResponses){
        this.setBookmarkResponses(bookmarkResponses);

        return this;
    }
}
