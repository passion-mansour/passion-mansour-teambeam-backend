package passionmansour.teambeam.model.dto.project.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import passionmansour.teambeam.model.dto.project.ProjectDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "프로젝트 정보")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ProjectDto project;

    @Schema(description = "프로젝트 리스트")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ProjectDto> projectList;
}
