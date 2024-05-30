package passionmansour.teambeam.model.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListResponse {
    private List<BoardResponse> boardResponses = new ArrayList<>();;

    public BoardListResponse form(List<BoardResponse> boardResponses){
        this.setBoardResponses(boardResponses);
        return this;
    }
}
