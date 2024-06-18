package passionmansour.teambeam.model.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNotificationRequest {

    private String title;
    private Long memberId;
    private Long projectId;
    private Long boardId;
    private Long postId;

}
