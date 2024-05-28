package passionmansour.teambeam.model.dto.project;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectJoinMemberDto {
    @Schema(description = "회원 고유 아이디")
    private Long memberId;
    @Schema(description = "회원 이름")
    private String memberName;
    @Schema(description = "메일 주소")
    private String mail;
    @Schema(description = "역할(BE, FE)")
    private String memberRole;
    @Schema(description = "호스트 여부")
    private boolean isHost;
}
