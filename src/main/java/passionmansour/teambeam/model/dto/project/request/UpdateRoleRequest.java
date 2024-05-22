package passionmansour.teambeam.model.dto.project.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.enums.MemberRole;

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
        private Long memberId;
        private MemberRole memberRole;
    }
}
