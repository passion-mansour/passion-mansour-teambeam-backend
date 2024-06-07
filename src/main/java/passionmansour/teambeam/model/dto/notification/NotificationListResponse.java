package passionmansour.teambeam.model.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationListResponse {

    private String message;
    private int notificationCount;
    private List<NotificationDto> notificationList;

}
