package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Board;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {
    private Long boardId;
    private String name;
    private Long projectId;
    private List<PostResponse> posts;

    public BoardResponse form(Board board){
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .name(board.getBoardName())
                .projectId(board.getProject().getProjectId())
                .posts(new PostListResponse().entityToForm(board.getPosts()).getPostResponses())
                .build();
    }
}
