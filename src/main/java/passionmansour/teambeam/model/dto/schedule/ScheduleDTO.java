package passionmansour.teambeam.model.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import passionmansour.teambeam.model.dto.todolist.dto.BottomTodoDTO;
import passionmansour.teambeam.model.entity.ScheduleMember;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {
    private Long ScheduleId;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime time;
    private String location;
    private String content;
    private String link;
    private List<ScheduleMemberDTO> scheduleMembers;

    @Data
    public class ScheduleMemberDTO{
        private Long memberId;
        private String memberName;
    }

    public void setScheduleMemberDTO(List<ScheduleMember> scheduleMemberList){
        this.scheduleMembers = new ArrayList<>();
        for (ScheduleMember scheduleMember : scheduleMemberList) {
            ScheduleMemberDTO scheduleDTO = new ScheduleMemberDTO();
            scheduleDTO.setMemberId(scheduleMember.getMember().getMemberId());
            scheduleDTO.setMemberName(scheduleMember.getMember().getMemberName());
            this.scheduleMembers.add(scheduleDTO);
        }
    }
}
