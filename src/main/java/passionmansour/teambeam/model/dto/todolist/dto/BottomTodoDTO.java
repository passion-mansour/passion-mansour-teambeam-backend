package passionmansour.teambeam.model.dto.todolist.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BottomTodoDTO {
    private Long bottomTodoId;
    private String title;
    private boolean status;
    private Date startDate;
    private Date endDate;
    private String memo;
    // getters and setters
}
