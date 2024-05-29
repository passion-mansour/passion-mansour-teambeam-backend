package passionmansour.teambeam.model.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetRequest {

    @Schema(description = "비밀번호 재설정 토큰")
    @NotNull(message = "token cannot be null")
    private String token;

    @Schema(description = "새로운 비밀번호")
    @NotNull(message = "newPassword cannot be null")
    private String newPassword;

}
