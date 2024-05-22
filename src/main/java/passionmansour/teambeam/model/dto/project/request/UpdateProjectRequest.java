package passionmansour.teambeam.model.dto.project.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import passionmansour.teambeam.model.enums.ProjectStatus;

@Data
public class UpdateProjectRequest {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String projectName;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ProjectStatus projectStatus;
}
