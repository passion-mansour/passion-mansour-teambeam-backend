package passionmansour.teambeam.model.dto.todolist.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PatchBottomTodoRequest {
    private Long middleTodoId;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String memo;
    private Boolean status;
    private Long member;
}
