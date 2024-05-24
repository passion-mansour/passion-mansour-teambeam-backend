package passionmansour.teambeam.model.dto.project.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.dto.project.ProjectJoinMemberDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectJoinMemberResponse {

    private String message;

    private List<ProjectJoinMemberDto> joinMemberList;


}
