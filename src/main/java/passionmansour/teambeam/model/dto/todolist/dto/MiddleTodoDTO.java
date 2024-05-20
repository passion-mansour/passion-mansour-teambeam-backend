package passionmansour.teambeam.model.dto.todolist.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MiddleTodoDTO {
    private Long middleTodoId;
    private String title;
    private boolean status;
    private Date startDate;
    private Date endDate;
    private List<BottomTodoDTO> bottomTodos;

}
