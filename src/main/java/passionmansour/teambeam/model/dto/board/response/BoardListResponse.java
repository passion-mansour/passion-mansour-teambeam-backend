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
    List<BoardResponse> boardResponses = new ArrayList<>();

    public BoardListResponse form(List<BoardResponse> boardResponses){
        return BoardListResponse.builder()
                .boardResponses(boardResponses)
                .build();
    }
}
