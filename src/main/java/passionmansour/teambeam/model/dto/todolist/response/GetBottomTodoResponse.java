package passionmansour.teambeam.model.dto.todolist.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import passionmansour.teambeam.model.dto.todolist.dto.BottomTodoDTO;

@Data
@AllArgsConstructor
public class GetBottomTodoResponse {
    private String status;
    private BottomTodoDTO data;

}
