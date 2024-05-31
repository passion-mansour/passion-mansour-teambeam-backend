package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Bookmark;
import passionmansour.teambeam.model.entity.Post;

import java.awt.print.Book;
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

    public BookmarkListResponse entityToForm(List<Bookmark> bookmarks){
        if(bookmarks != null) {
            for (Bookmark bookmark : bookmarks) {
                this.bookmarkResponses.add(new BookmarkResponse().form(bookmark));
            }
        }

        return this;
    }
}
