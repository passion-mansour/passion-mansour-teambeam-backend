package passionmansour.teambeam.model.dto.project;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import passionmansour.teambeam.model.enums.ProjectStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private Long projectId;

    @NotNull(message = "ProjectName cannot be null")
    private String projectName;

    @NotNull(message = "description cannot be null")
    private String description;

    private ProjectStatus projectStatus;

    private LocalDateTime createDate;

}
