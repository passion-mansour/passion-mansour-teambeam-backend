package passionmansour.teambeam.model.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Schedule;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetScheduleResponse {
    private int status;
    private ScheduleDTO data;
}
