package passionmansour.teambeam.model.dto.todolist.request;

import lombok.Data;

import java.util.Date;

@Data
public class PatchMiddleTodoRequest {
    private Long topTodoId;
    private String title;
    private Date startDate;
    private Date endDate;
    private boolean status;
}
