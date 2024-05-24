package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Board;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.entity.Project;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {
    private Long boardId;
    private String name;
    private Project project;
    private List<Post> posts = new ArrayList<>();

    public BoardResponse form(Board board){
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .name(board.getBoardName())
                .project(board.getProject())
                .posts(board.getPosts())
                .build();
    }
}
