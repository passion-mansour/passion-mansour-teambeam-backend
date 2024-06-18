package passionmansour.teambeam.model.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteNotificationRequest {

    private Long memberId;
    private List<Long> notificationIds;

}
