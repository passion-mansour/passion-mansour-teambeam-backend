package passionmansour.teambeam.model.dto.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import passionmansour.teambeam.model.enums.StartPage;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    @Schema(description = "회원 고유 아이디")
    private Long memberId;
    @Schema(description = "메일 주소")
    private String mail;
    @Schema(description = "비밀번호")
    private String password;
    @Schema(description = "회원 이름")
    private String memberName;
    @Schema(description = "알림 수")
    private int notificationCount;
    @Schema(description = "프로필 이미지")
    private String profileImage;
    @Schema(description = "시작 페이지")
    private StartPage startPage;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String accessToken;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String refreshToken;

}
