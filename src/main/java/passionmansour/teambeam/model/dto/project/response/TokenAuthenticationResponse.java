package passionmansour.teambeam.model.dto.project.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TokenAuthenticationResponse {

    @Schema(description = "응답 메시지")
    private String message;
    @Schema(description = "회원 여부")
    private boolean isMember;
    @Schema(description = "비회원의 경우 토큰")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String token;
}
