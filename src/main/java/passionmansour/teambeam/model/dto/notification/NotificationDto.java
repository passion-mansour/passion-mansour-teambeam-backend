package passionmansour.teambeam.model.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    @Schema(description = "프로젝트 고유 아이디")
    private Long projectId;
    @Schema(description = "프로젝트 이름")
    private String projectName;
    @Schema(description = "알림 고유 아이디")
    private Long notificationId;
    @Schema(description = "알림 내용")
    private String notificationContent;
    @Schema(description = "확인 유무")
    private boolean isRead;

}
