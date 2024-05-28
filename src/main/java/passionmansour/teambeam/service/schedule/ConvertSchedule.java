package passionmansour.teambeam.service.schedule;

import org.springframework.stereotype.Service;
import passionmansour.teambeam.model.dto.schedule.ScheduleDTO;
import passionmansour.teambeam.model.dto.schedule.ScheduleTopTodoDTO;
import passionmansour.teambeam.model.dto.todolist.dto.TopTodoDTO;
import passionmansour.teambeam.model.entity.Schedule;
import passionmansour.teambeam.model.entity.TopTodo;

import java.util.stream.Collectors;

@Service
public class ConvertSchedule {
    public ScheduleDTO convertSchedule(Schedule schedule){
        ScheduleDTO dto = new ScheduleDTO();
        dto.setScheduleId(schedule.getScheduleId());
        dto.setLink(schedule.getScheduleLink());
        dto.setTitle(schedule.getScheduleTitle());
        dto.setTime(schedule.getScheduleTime());
        dto.setContent(schedule.getScheduleContent());
        dto.setLocation(schedule.getScheduleLocate());
        dto.setScheduleMemberDTO(schedule.getScheduleMembers());

        return dto;
    }

    public ScheduleTopTodoDTO convertTopTodo(TopTodo topTodo){
        ScheduleTopTodoDTO dto = new ScheduleTopTodoDTO();
        dto.setTopTodoId(topTodo.getTopTodoId());
        dto.setTitle(topTodo.getTopTodoTitle());
        dto.setStatus(topTodo.isTopTodoStatus());
        dto.setStartDate(topTodo.getStartDate());
        dto.setEndDate(topTodo.getEndDate());

        return dto;
    }
}
