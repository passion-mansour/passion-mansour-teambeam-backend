package passionmansour.teambeam.model.dto.mypage.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.dto.todolist.dto.BottomTodoDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMyTodoResponse {
    private Long projectId;
    private String projectName;
    private List<BottomTodoDTO> todos;
}
