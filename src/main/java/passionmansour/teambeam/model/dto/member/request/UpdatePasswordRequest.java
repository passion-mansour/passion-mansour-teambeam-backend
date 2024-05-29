package passionmansour.teambeam.model.dto.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UpdatePasswordRequest {

    @Schema(description = "기존 비밀번호")
    private String oldPassword;
    @Schema(description = "새로운 비밀번호")
    private String newPassword;

}
