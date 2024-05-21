package passionmansour.teambeam.model.dto.todolist.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PostMiddleTodoRequest {
    private Long topTodoId;
    private String title;
    private Date startDate;
    private Date endDate;
}
