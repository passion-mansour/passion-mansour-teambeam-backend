package passionmansour.teambeam.model.dto.todolist.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import passionmansour.teambeam.model.dto.todolist.dto.TopTodoDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class GetTodolistResponse {
    private String status;
    private List<TopTodoDTO> todos;

}
