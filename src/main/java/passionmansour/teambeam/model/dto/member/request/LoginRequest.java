package passionmansour.teambeam.model.dto.member.request;

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
public class LoginRequest {

    @Schema(description = "메일 주소")
    @NotNull(message = "mail cannot be null")
    private String mail;

    @Schema(description = "비밀번호")
    @NotNull(message = "password cannot be null")
    private String password;
}