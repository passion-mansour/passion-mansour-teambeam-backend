package passionmansour.teambeam.model.dto.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectJoinMemberDto {
    private Long memberId;
    private String memberName;
    private String mail;
    private String memberRole;
    private boolean isHost;
}
