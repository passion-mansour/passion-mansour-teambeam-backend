package passionmansour.teambeam.model.dto.project.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import passionmansour.teambeam.model.enums.ProjectStatus;

@Data
public class UpdateProjectRequest {

    @Schema(description = "프로젝트 이름")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String projectName;

    @Schema(description = "프로젝트 설명")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;

    @Schema(description = "프로젝트 진행 상태(PROGRESS, END)")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ProjectStatus projectStatus;

}
