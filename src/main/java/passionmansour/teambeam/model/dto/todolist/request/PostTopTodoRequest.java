package passionmansour.teambeam.model.dto.todolist.request;

import lombok.Data;

import java.util.Date;

@Data
public class PostTopTodoRequest {
    private String title;
    private Date startDate;
    private Date endDate;

}
