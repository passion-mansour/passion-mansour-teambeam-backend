package passionmansour.teambeam.model.dto.member.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Schema(description = "회원 이름")
    @NotNull(message = "Name cannot be null")
    private String memberName;

    @Schema(description = "메일 주소")
    @NotNull(message = "mail cannot be null")
    private String mail;

    @Schema(description = "비밀번호")
    @NotNull(message = "password cannot be null")
    private String password;

    @Schema(description = "초대를 받은 사용자의 경우 초대 토큰")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String token;

}