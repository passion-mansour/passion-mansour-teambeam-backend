package passionmansour.teambeam.model.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReadStatusRequest {

    private Long memberId;
    private Long notificationId;

}
