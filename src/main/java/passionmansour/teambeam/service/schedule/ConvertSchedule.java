package passionmansour.teambeam.service.schedule;

import org.springframework.stereotype.Service;
import passionmansour.teambeam.model.dto.schedule.ScheduleDTO;
import passionmansour.teambeam.model.entity.Schedule;

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
}
