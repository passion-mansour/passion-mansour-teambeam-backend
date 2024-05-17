package passionmansour.teambeam.model.dto.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "엑세스 토큰")
    private String accessToken;

    @Schema(description = "리프레시 토큰")
    private String refreshToken;

    @Schema(description = "회원 ID")
    private Long memberId;

}
