package passionmansour.teambeam.model.dto.todolist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import passionmansour.teambeam.model.entity.Member;

import java.util.Date;

@Data
public class BottomTodoDTO {
    private Long bottomTodoId;
    private String title;
    private boolean status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;
    private String memo;
    private Member member;
}
