package passionmansour.teambeam.model.dto.todolist.request;

import lombok.Data;

import java.util.Date;

@Data
public class PostBottomTodoRequest {
    private Long middleTodoId;
    private String title;
    private Date startDate;
    private Date endDate;
    private String content;
}
