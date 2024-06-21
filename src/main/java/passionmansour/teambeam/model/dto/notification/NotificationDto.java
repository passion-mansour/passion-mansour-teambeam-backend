package passionmansour.teambeam.model.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.entity.Notification;

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
    private String title;
    @Schema(description = "확인 유무")
    private boolean isRead;
    @Schema(description = "알림 유형")
    private Notification.Type type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long postId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long boardId;

}
