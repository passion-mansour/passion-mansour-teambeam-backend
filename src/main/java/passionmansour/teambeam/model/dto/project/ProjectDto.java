package passionmansour.teambeam.model.dto.project;


import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "프로젝트 고유 아이디")
    private Long projectId;

    @Schema(description = "프로젝트 이름")
    @NotNull(message = "ProjectName cannot be null")
    private String projectName;

    @Schema(description = "프로젝트 설명")
    @NotNull(message = "description cannot be null")
    private String description;

    @Schema(description = "프로젝트 진행 상태(PROGRESS, END)")
    private ProjectStatus projectStatus;

    @Schema(description = "셍성 날짜")
    private LocalDateTime createDate;

}
