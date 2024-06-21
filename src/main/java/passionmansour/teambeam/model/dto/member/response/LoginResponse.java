package passionmansour.teambeam.model.dto.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.enums.StartPage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "회원 ID")
    private Long memberId;

    @Schema(description = "시작 페이지")
    private StartPage startPage;

}
