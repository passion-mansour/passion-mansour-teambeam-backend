package passionmansour.teambeam.model.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import passionmansour.teambeam.model.entity.Project;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ScheduleTopTodoDTO {
    private Long topTodoId;
    private Long projectId;
    private String title;
    private boolean status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
