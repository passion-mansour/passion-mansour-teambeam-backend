package passionmansour.teambeam.model.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSocketDto {
    private Long memberId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private NotificationDto notification;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<NotificationDto> notificationList;
}
