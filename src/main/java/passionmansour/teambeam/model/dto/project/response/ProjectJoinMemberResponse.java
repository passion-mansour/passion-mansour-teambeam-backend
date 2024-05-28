package passionmansour.teambeam.model.dto.project.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.dto.project.ProjectJoinMemberDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectJoinMemberResponse {

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "참여 멤버 리스트")
    private List<ProjectJoinMemberDto> joinMemberList;


}
