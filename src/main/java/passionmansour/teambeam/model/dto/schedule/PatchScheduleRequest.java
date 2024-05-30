package passionmansour.teambeam.model.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
public class PatchScheduleRequest {
    private Long scheduleId;
    private String title;
    private LocalDateTime time;
    private String location;
    private String content;
    private String link;
    private List<Long> memberId;
}
