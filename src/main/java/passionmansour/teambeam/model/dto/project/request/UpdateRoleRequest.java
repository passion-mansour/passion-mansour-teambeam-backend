package passionmansour.teambeam.model.dto.project.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleRequest {

    private List<MemberRoles> members;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberRoles {

        @Schema(description = "회원 고유 아이디")
        private Long memberId;
        @Schema(description = "역할")
        private String memberRole;

    }
}
