package passionmansour.teambeam.model.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.dto.todolist.dto.TopTodoDTO;
import passionmansour.teambeam.model.entity.Schedule;
import passionmansour.teambeam.model.entity.TopTodo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCalendarResponse {
    private String status;
    private List<ScheduleTopTodoDTO> topTodos;
    private List<ScheduleDTO> schedules;

}
