package passionmansour.teambeam.model.dto.todolist.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import passionmansour.teambeam.service.todolist.CustomDateDeserializer;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PostTopTodoRequest {
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
